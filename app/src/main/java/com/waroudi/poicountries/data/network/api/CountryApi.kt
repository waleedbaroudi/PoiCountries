package com.waroudi.poicountries.data.network.api

import com.waroudi.poicountries.data.models.api.Country
import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {

    /**
     * Retrieve all countries from the service
     * @return list of retrieved countries
     */
    @GET("europe")
    suspend fun getAllCountries(): Response<List<Country>>

}