package test.revolut.com.revoluttest.module.currencies

import android.os.Bundle
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.data_source.ListDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import test.revolut.com.revoluttest.module.Currency
import test.revolut.com.revoluttest.module.CurrencyRatesRepository
import test.revolut.com.revoluttest.module.currencies.item.CurrencyClickListener
import test.revolut.com.revoluttest.module.currencies.item.CurrencyItem
import test.revolut.com.revoluttest.module.currencies.ui.CurrenciesView
import test.revolut.com.revoluttest.utils.SchedulersFactory
import test.revolut.com.revoluttest.utils.asArrayList
import java.math.BigDecimal
import java.math.RoundingMode

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
        private val repository: CurrencyRatesRepository,
        savedState: Bundle?
) : CurrenciesPresenter, CurrencyClickListener {

    private var disposables = CompositeDisposable()
    private var view: CurrenciesView? = null
    private var currencyItems: MutableList<CurrencyItem>? = savedState?.getParcelableArrayList(KEY_ITEMS)
    private var cachedPair: Pair<String, BigDecimal>? = savedState?.getSerializable(KEY_PAIR) as? Pair<String, BigDecimal>
    private var pollingDisposable: Disposable? = null

    override fun onSaveState(): Bundle = Bundle().apply {
        putParcelableArrayList(KEY_ITEMS, currencyItems.asArrayList())
        putSerializable(KEY_PAIR, cachedPair)
    }

    override fun attachView(view: CurrenciesView) {
        this.view = view
        val items = currencyItems ?: buildInitialData()
        currencyItems = items.toMutableList()
        updateDataSource(items)
//        pollingDisposable = interactor.startPolling()
//                .observeOn(schedulersFactory.mainThread())
//                .subscribe { }
        pollingDisposable = interactor.startPolling()
                .observeOn(schedulersFactory.mainThread())
                .subscribe {
                    cachedPair?.let {
                        updateItems(it)
                    }
                }
    }

    private fun subscribe() {
        disposables.add(currencyObservable
                .subscribeOn(schedulersFactory.mainThread())
                .observeOn(schedulersFactory.mainThread())
                .subscribe {
                    cachedPair = it
                    updateItems(it)
                })
//
//        disposables.add(interactor.startPolling()
//                .observeOn(schedulersFactory.mainThread())
//                .subscribe {
//                    cachedPair?.let {
//                        updateItems(it)
//                    }
//                })
    }

    private fun updateItems(pair: Pair<String, BigDecimal>) {
        val rates = repository.getCurrencyRates()
        val selectedCurrency = pair.first
        val selectedCurrencyCount = pair.second
        if (rates != null) {
            currencyItems?.forEach {
                val thisCurrency = it.stringId
                val thisCurrencyValue = rates[thisCurrency]
                val selectedCurrencyValue = rates[selectedCurrency]
                if (thisCurrencyValue != null && selectedCurrencyValue != null) {
                    val thisCurrencyCount = thisCurrencyValue.divide(selectedCurrencyValue, 2, RoundingMode.CEILING).multiply(selectedCurrencyCount)
                    it.value = thisCurrencyCount.setScale(2, RoundingMode.CEILING)
                }
            }
        }
        currencyItems?.let {
            updateDataSourceRange(it)
        }
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
        subscribe()
    }

    private fun updateDataSourceRange(items: List<CurrencyItem>) {
        val dataSource = ListDataSource(items)
        adapterPresenter.onDataSourceChanged(dataSource)
        view?.onDataRangeChanged(1, items.size)
    }

    override fun onCurrencyClicked(item: CurrencyItem) {
        disposables.clear()
        val items = currencyItems ?: return
        items.forEach {
            it.isMainCurrency = false
        }
        item.isMainCurrency = true
        if (items.remove(item)) {
            items.add(0, item)
        }
        currencyItems = items
        updateDataSource(items)
        subscribe()
    }

    private fun buildInitialData(): List<CurrencyItem> {
        return Currency.values().map {
            CurrencyItem(it.name)
        }
    }

}

private const val KEY_PAIR = "key_pair"
private const val KEY_ITEMS = "key_items"