package test.revolut.com.revoluttest.module

import java.math.BigDecimal

interface CurrencyRatesRepository {

    fun getCurrencyRates(): Map<String, BigDecimal>?

}

interface CurrencyRatesUpdater {

    fun updateRates(baseCurrency: String, rates: Map<String, BigDecimal>)

}

class CurrencyRatesRepositoryImpl : CurrencyRatesRepository, CurrencyRatesUpdater {

    @Volatile
    private var rates: Map<String, BigDecimal>? = null

    override fun updateRates(baseCurrency: String, rates: Map<String, BigDecimal>) {
        synchronized(lock) {
            this.rates = rates
        }
    }

    override fun getCurrencyRates(): Map<String, BigDecimal>? {
        synchronized(lock) {
            return rates
        }
    }

}

private val lock = Any()