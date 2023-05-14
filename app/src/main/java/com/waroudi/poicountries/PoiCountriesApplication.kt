package com.waroudi.poicountries

import android.app.Application
import com.waroudi.poicountries.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PoiCountriesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Koin and load modules
        startKoin {
            androidContext(this@PoiCountriesApplication)
            modules(listOf(appModule))
        }
    }
}