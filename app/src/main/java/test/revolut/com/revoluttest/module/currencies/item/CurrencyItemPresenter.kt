package test.revolut.com.revoluttest.module.currencies.item

import com.avito.konveyor.blueprint.ItemPresenter
import dagger.Lazy
import io.reactivex.subjects.Subject
import java.math.BigDecimal

interface CurrencyItemPresenter : ItemPresenter<CurrencyItemView, CurrencyItem>

class CurrencyItemPresenterImpl(private val currencyClickListener: Lazy<CurrencyClickListener>,
                                private val onValueChangePublisher: Subject<Pair<String, BigDecimal>>
) : CurrencyItemPresenter {

    override fun bindView(view: CurrencyItemView, item: CurrencyItem, position: Int) {
        with(view) {
            setCurrencyName(item.stringId)
            val cachedValue = item.value
            if (cachedValue != null) {
                setCurrencyValue(cachedValue.toString())
            } else {
                setCurrencyValue("")
            }
            setEditTextEditable(item.isMainCurrency)
            if (item.isMainCurrency) {
                setOnValueChangesListener {
                    item.value = it
                    onValueChangePublisher.onNext(Pair(item.stringId, it))
                }
                setOnClickListener {}
            } else {
                removesetOnValueChangesListener()
                setOnClickListener {
                    currencyClickListener.get().onCurrencyClicked(item)
                }
            }
        }

    }

}