package com.waroudi.poicountries.data.network.services

import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.data.models.api.PoiResult
import com.waroudi.poicountries.data.models.error.PoiServiceError
import com.waroudi.poicountries.data.network.api.CountryApi
import com.waroudi.poicountries.utils.extensions.convertError
import com.waroudi.poicountries.utils.extensions.convertErrorBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Manages data through the country service
 * @param countryApi a [CountryApi] to provide service methods
 */
class CountryService(private val countryApi: CountryApi) {

    /**
     * Retrieve all countries from the country service
     * @return list of retrieved countries
     */
    fun getAllCountries(): Flow<PoiResult<List<Country>>> {
        return flow {
            val response = countryApi.getAllCountries()
            val body = response.body()
            val error = response.convertErrorBody<PoiServiceError>()
            emit(PoiResult(body, error))
        }.convertError()
    }
}