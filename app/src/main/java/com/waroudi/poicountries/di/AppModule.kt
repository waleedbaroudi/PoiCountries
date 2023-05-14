package com.waroudi.poicountries.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.waroudi.poicountries.data.local.PoiLocalStorage
import com.waroudi.poicountries.data.network.api.CountryApi
import com.waroudi.poicountries.data.network.services.CountryService
import com.waroudi.poicountries.data.repositories.CountriesRepository
import com.waroudi.poicountries.ui.country_details.CountryDetailsViewModel
import com.waroudi.poicountries.ui.countries.CountriesViewModel
import com.waroudi.poicountries.ui.main.MainViewModel
import com.waroudi.poicountries.utils.Constants
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "poicountries_store")

/**
 * Koin module containing all app dependencies
 */
val appModule = module {

    // API
    factory {
        // Set timeout configurations
        val client = OkHttpClient.Builder()
            .writeTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl(Constants.BACKEND_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(CountryApi::class.java) }

    // Services
    single { CountryService(get()) }

    // Local Storages
    single { PoiLocalStorage(androidApplication().dataStore) }

    // Repositories
    single { CountriesRepository(get(), get()) }

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { CountriesViewModel(get()) }
    viewModel { CountryDetailsViewModel(get()) }
}
