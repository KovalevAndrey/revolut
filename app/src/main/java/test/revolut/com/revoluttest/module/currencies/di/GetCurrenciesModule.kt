package test.revolut.com.revoluttest.module.currencies.di

import android.content.Context
import android.os.Bundle
import com.avito.konveyor.ItemBinder
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.adapter.SimpleAdapterPresenter
import dagger.Lazy
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import test.revolut.com.revoluttest.application.api.RevolutApi
import test.revolut.com.revoluttest.module.CurrencyRatesRepositoryImpl
import test.revolut.com.revoluttest.module.CurrencyRatesUpdater
import test.revolut.com.revoluttest.module.currencies.*
import test.revolut.com.revoluttest.module.currencies.item.CurrencyClickListener
import test.revolut.com.revoluttest.module.currencies.item.CurrencyItemBlueprint
import test.revolut.com.revoluttest.module.currencies.item.CurrencyItemPresenter
import test.revolut.com.revoluttest.module.currencies.item.CurrencyItemPresenterImpl
import test.revolut.com.revoluttest.utils.SchedulersFactory
import java.math.BigDecimal

@Module
class GetCurrenciesModule(private val context: Context,
                          private val presenterState: Bundle?) {

    private val currentValueSubject = PublishSubject.create<Pair<String, BigDecimal>>()

    @Provides
    @PerActivity
    internal fun providePresenter(interactor: GetCurrenciesInteractor,
                                  schedulersFactory: SchedulersFactory,
                                  currencyValueTransformer: CurrencyValueTransformer,
                                  adapterPresenter: AdapterPresenter): CurrenciesPresenterImpl {
        return CurrenciesPresenterImpl(interactor, schedulersFactory,
                adapterPresenter, currentValueSubject, currencyValueTransformer, presenterState)
    }

    @Provides
    @PerActivity
    internal fun provideCurrencyValueTransformer(repository: CurrencyRatesRepositoryImpl): CurrencyValueTransformer {
        return CurrencyValueTransformerImpl(repository)
    }

    @Provides
    @PerActivity
    internal fun provideCurrencyRatesRepository(): CurrencyRatesRepositoryImpl {
        return CurrencyRatesRepositoryImpl()
    }

    @Provides
    @PerActivity
    internal fun provideCurrencyRatesUpdater(repository: CurrencyRatesRepositoryImpl): CurrencyRatesUpdater {
        return repository
    }

    @Provides
    @PerActivity
    internal fun provideItemClickListener(presenter: CurrenciesPresenterImpl): CurrencyClickListener {
        return presenter
    }

    @Provides
    @PerActivity
    internal fun provideGetFlightsInteractorImpl(api: RevolutApi,
                                                 schedulersFactory: SchedulersFactory,
                                                 updater: CurrencyRatesUpdater): GetCurrenciesInteractor {
        return GetCurrenciesInteractorImpl(api, schedulersFactory, updater)
    }


    @Provides
    @PerActivity
    internal fun provideAdapterPresenter(itemBinder: ItemBinder): AdapterPresenter {
        return SimpleAdapterPresenter(itemBinder, itemBinder)
    }

    @Provides
    @PerActivity
    internal fun provideItineraryItemPresenter(currencyClickListener: Lazy<CurrencyClickListener>): CurrencyItemPresenter {
        return CurrencyItemPresenterImpl(currencyClickListener, currentValueSubject)
    }

    @Provides
    @PerActivity
    internal fun provideItemBlueprint(currencyItemPresenter: CurrencyItemPresenter): CurrencyItemBlueprint {
        return CurrencyItemBlueprint(currencyItemPresenter)
    }

    @Provides
    @PerActivity
    internal fun provideItemBinder(itemBlueprint: CurrencyItemBlueprint): ItemBinder {
        val builder = ItemBinder.Builder()
                .registerItem(itemBlueprint)
        return builder.build()
    }
}
