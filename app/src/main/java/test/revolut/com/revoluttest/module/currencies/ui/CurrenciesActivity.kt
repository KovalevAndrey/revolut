package test.revolut.com.revoluttest.module.currencies.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.avito.konveyor.ItemBinder
import com.avito.konveyor.adapter.AdapterPresenter
import test.revolut.com.revoluttest.R
import test.revolut.com.revoluttest.application.TestApp
import test.revolut.com.revoluttest.module.currencies.CurrenciesPresenterImpl
import test.revolut.com.revoluttest.module.currencies.di.GetCurrenciesModule
import javax.inject.Inject

class CurrenciesActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: CurrenciesPresenterImpl
    @Inject
    lateinit var adapterPresenter: AdapterPresenter
    @Inject
    lateinit var itemBinder: ItemBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val presenterState = savedInstanceState?.getBundle(KEY_PRESENTER)
        setContentView(R.layout.currency_activity)
        TestApp.instance
                .component
                .plus(GetCurrenciesModule(this.baseContext, presenterState))
                .inject(this)
        val currencyView = CurrenciesViewImpl(findViewById(R.id.content), itemBinder, adapterPresenter)
        presenter.attachView(currencyView)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBundle(KEY_PRESENTER, presenter.onSaveState())
    }
}

private const val KEY_PRESENTER = "key_presenter"