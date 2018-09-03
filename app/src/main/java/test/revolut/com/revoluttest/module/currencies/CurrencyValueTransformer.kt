package test.revolut.com.revoluttest.module.currencies

import test.revolut.com.revoluttest.module.CurrencyRatesRepository
import test.revolut.com.revoluttest.module.currencies.item.CurrencyItem
import java.math.BigDecimal
import java.math.RoundingMode

interface CurrencyValueTransformer {

    fun transformItems(newCurrencyValue: Pair<String, BigDecimal>, items: List<CurrencyItem>): List<CurrencyItem>
}


class CurrencyValueTransformerImpl(private val repository: CurrencyRatesRepository) : CurrencyValueTransformer {

    override fun transformItems(newCurrencyValue: Pair<String, BigDecimal>, items: List<CurrencyItem>): List<CurrencyItem> {
        val rates = repository.getCurrencyRates()
        val selectedCurrency = newCurrencyValue.first
        val selectedCurrencyCount = newCurrencyValue.second
        if (rates != null) {
            items.forEach {
                val thisCurrency = it.stringId
                val thisCurrencyValue = rates[thisCurrency]
                val selectedCurrencyValue = rates[selectedCurrency]
                if (thisCurrencyValue != null && selectedCurrencyValue != null) {
                    val thisCurrencyCount = thisCurrencyValue.divide(selectedCurrencyValue, 2, RoundingMode.CEILING).multiply(selectedCurrencyCount)
                    it.value = thisCurrencyCount.setScale(2, RoundingMode.CEILING)
                }
            }
        }
        return items
    }


}