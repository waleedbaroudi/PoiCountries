package com.waroudi.poicountries.data.models.api

import com.waroudi.poicountries.data.models.error.PoiServiceError

data class PoiResult<D>(val data: D?, val error: PoiServiceError? = null)
