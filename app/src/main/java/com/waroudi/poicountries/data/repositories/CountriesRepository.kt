package com.waroudi.poicountries.data.repositories

import com.waroudi.poicountries.data.local.PoiLocalStorage
import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.data.models.api.PoiResult
import com.waroudi.poicountries.data.network.services.CountryService
import com.waroudi.poicountries.ui.components.dialogs.Sorting
import kotlinx.coroutines.flow.Flow

/**
 * A repository to handle country data locally and through the country service
 * @param service the country service for backend operations
 * @param localStore the local storage for local operations
 */
class CountriesRepository(private val service: CountryService, private val localStore: PoiLocalStorage) {
    // Service
    fun getAllCountries(): Flow<PoiResult<List<Country>>> = service.getAllCountries()

    // Local
    fun setRecentSorting(sorting: Sorting) {
        localStore.setRecentSorting(sorting)
    }

    fun getRecentSorting() = localStore.getRecentSorting()

}