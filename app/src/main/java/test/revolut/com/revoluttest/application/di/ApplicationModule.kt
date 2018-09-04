package test.revolut.com.revoluttest.application.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import test.revolut.com.revoluttest.application.TestApp
import test.revolut.com.revoluttest.module.CurrencyRatesRepositoryImpl
import test.revolut.com.revoluttest.utils.SchedulersFactory
import test.revolut.com.revoluttest.utils.SchedulersFactoryImpl
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class ApplicationModule(private val app: TestApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideAvitoApp(): TestApp {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app.applicationContext
    }


    @Provides
    @Singleton
    fun provideSchedulersFactory(): SchedulersFactory {
        return SchedulersFactoryImpl()
    }

    @Provides
    @Singleton
    internal fun provideCurrencyRatesRepository(): CurrencyRatesRepositoryImpl {
        return CurrencyRatesRepositoryImpl()
    }

}