package test.revolut.com.revoluttest.application

import android.app.Application
import test.revolut.com.revoluttest.application.di.ApplicationComponent
import test.revolut.com.revoluttest.application.di.ApplicationModule
import test.revolut.com.revoluttest.application.di.DaggerApplicationComponent

class TestApp : Application() {

    lateinit var component: ApplicationComponent

    companion object {
        lateinit var instance: TestApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component.inject(this)
    }

}