package test.revolut.com.revoluttest.application.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import test.revolut.com.revoluttest.application.api.GsonFactory
import test.revolut.com.revoluttest.application.api.RevolutApi
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonFactory().createGson()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context,
                            loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loggingInterceptor)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient,
                   gson: Gson): RevolutApi {

        val builder = Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
        return builder
                .build()
                .create(RevolutApi::class.java)

    }

}

private const val CACHE_DIR = "http"
private const val HOST = "https://revolut.duckdns.org/"