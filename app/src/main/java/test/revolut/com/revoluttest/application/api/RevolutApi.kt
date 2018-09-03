package test.revolut.com.revoluttest.application.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import test.revolut.com.revoluttest.application.api.model.CurrenciesResult

interface RevolutApi {

    @GET("latest")
    fun get(@Query("base") currency: String = EUR_CURRENCY): Observable<CurrenciesResult>

}

const val EUR_CURRENCY = "EUR"