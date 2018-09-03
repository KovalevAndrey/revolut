package test.revolut.com.revoluttest.application.api.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class CurrenciesResult(@SerializedName("base") val baseCurrency: String,
                       @SerializedName("rates") val rates: Map<String, BigDecimal>)