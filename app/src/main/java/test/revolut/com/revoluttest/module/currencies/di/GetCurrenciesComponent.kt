package test.revolut.com.revoluttest.module.currencies.di

import dagger.Subcomponent
import test.revolut.com.revoluttest.module.currencies.ui.CurrenciesActivity

@Subcomponent(modules = [GetCurrenciesModule::class])
@PerActivity
interface GetCurrenciesComponent {

    fun inject(activity: CurrenciesActivity)
}