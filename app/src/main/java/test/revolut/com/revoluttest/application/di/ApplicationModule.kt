package test.revolut.com.revoluttest.application.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import test.revolut.com.revoluttest.utils.SchedulersFactory
import test.revolut.com.revoluttest.utils.SchedulersFactoryImpl
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class ApplicationModule(private val app: test.revolut.com.revoluttest.application.TestApp) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideAvitoApp(): test.revolut.com.revoluttest.application.TestApp {
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

}