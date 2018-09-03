package test.revolut.com.revoluttest.application.di

import dagger.Component
import test.revolut.com.revoluttest.application.TestApp
import test.revolut.com.revoluttest.module.currencies.di.GetCurrenciesComponent
import test.revolut.com.revoluttest.module.currencies.di.GetCurrenciesModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {

    fun inject(app: TestApp)

    fun plus(getFlightsModule: GetCurrenciesModule): GetCurrenciesComponent

}