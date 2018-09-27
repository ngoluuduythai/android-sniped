package com.sniped

import android.app.Application
import android.content.Context
import android.support.multidex.BuildConfig
import android.support.multidex.MultiDex
import com.facebook.soloader.SoLoader
import com.facebook.sonar.android.AndroidSonarClient
import com.facebook.sonar.android.utils.SonarUtils
import com.sniped.di.AppComponent
import com.sniped.di.AppModule
import com.sniped.di.DaggerAppComponent

open class App : Application() {

    protected lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
        attachFlipper()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    protected open fun initDagger() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        appComponent.inject(this)
    }

    private fun attachFlipper() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && SonarUtils.shouldEnableSonar(this)) {
            val client = AndroidSonarClient.getInstance(this)
            client.start()
        }
    }

    companion object {

        fun getAppComponent(context: Context) = (context.applicationContext as App).appComponent
    }
}