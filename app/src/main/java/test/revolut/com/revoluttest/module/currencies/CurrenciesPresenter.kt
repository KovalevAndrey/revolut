package test.revolut.com.revoluttest.module.currencies

import android.os.Bundle
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.data_source.ListDataSource
import com.petertackage.kotlinoptions.filterNotNone
import com.petertackage.kotlinoptions.optionOf
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import test.revolut.com.revoluttest.module.Currency
import test.revolut.com.revoluttest.module.currencies.item.CurrencyClickListener
import test.revolut.com.revoluttest.module.currencies.item.CurrencyItem
import test.revolut.com.revoluttest.module.currencies.ui.CurrenciesView
import test.revolut.com.revoluttest.utils.SchedulersFactory
import test.revolut.com.revoluttest.utils.asArrayList
import java.math.BigDecimal

interface CurrenciesPresenter {

    fun attachView(view: CurrenciesView)

    fun detachView()

    fun onSaveState(): Bundle

}

class CurrenciesPresenterImpl(
        private val interactor: GetCurrenciesInteractor,
        private val schedulersFactory: SchedulersFactory,
        private val adapterPresenter: AdapterPresenter,
        private val currencyObservable: PublishSubject<Pair<String, BigDecimal>>,
        private val currencyValueTransformer: CurrencyValueTransformer,
        savedState: Bundle?
) : CurrenciesPresenter, CurrencyClickListener {

    private var disposables = CompositeDisposable()
    private var view: CurrenciesView? = null
    private var currencyItems: List<CurrencyItem> = savedState?.getParcelableArrayList(KEY_ITEMS)
    ?: buildInitialData()
    private var cachedPair: Pair<String, BigDecimal>? = savedState?.getSerializable(KEY_PAIR) as? Pair<String, BigDecimal>
    private var pollingDisposable: Disposable? = null

    override fun onSaveState(): Bundle = Bundle().apply {
        putParcelableArrayList(KEY_ITEMS, currencyItems.asArrayList())
        putSerializable(KEY_PAIR, cachedPair)
    }

    override fun attachView(view: CurrenciesView) {
        this.view = view
        updateDataSource(currencyItems)
        subscribeToValueChanges()
        startPolling()
    }

    private fun startPolling() {
        pollingDisposable = interactor.startPolling()
                .map {
                    optionOf(cachedPair?.let {
                        currencyValueTransformer.transformItems(it, currencyItems)
                    })
                }
                .filterNotNone()
                .observeOn(schedulersFactory.mainThread())
                .subscribe { items ->
                    updateDataSourceRange(items)
                }
    }

    private fun subscribeToValueChanges() {
        disposables.add(currencyObservable
                .map { newSource ->
                    cachedPair = newSource
                    optionOf(currencyValueTransformer.transformItems(newSource, currencyItems))
                }
                .filterNotNone()
                .subscribeOn(schedulersFactory.mainThread())
                .observeOn(schedulersFactory.mainThread())
                .subscribe { items ->
                    items?.let {
                        updateDataSourceRange(it)
                    }
                })
    }

    override fun detachView() {
        pollingDisposable?.dispose()
        disposables.clear()
        this.view = null
    }

    private fun updateDataSource(items: List<CurrencyItem>) {
        val dataSource = ListDataSource(items)
        adapterPresenter.onDataSourceChanged(dataSource)
        view?.onDataSourceChanged()
    }

    private fun updateDataSourceRange(items: List<CurrencyItem>) {
        val dataSource = ListDataSource(items)
        adapterPresenter.onDataSourceChanged(dataSource)
        view?.onDataRangeChanged(1, items.size)
    }

    override fun onCurrencyClicked(item: CurrencyItem) {
        disposables.clear()
        currencyItems.forEach {
            it.isMainCurrency = false
        }
        item.isMainCurrency = true
        val items = currencyItems.toMutableList()
        if (items.remove(item)) {
            items.add(0, item)
        }
        currencyItems = items
        updateDataSource(currencyItems)
        subscribeToValueChanges()
    }

    private fun buildInitialData(): List<CurrencyItem> {
        return Currency.values().map {
            CurrencyItem(it.name)
        }
    }

}

private const val KEY_PAIR = "key_pair"
private const val KEY_ITEMS = "key_items"