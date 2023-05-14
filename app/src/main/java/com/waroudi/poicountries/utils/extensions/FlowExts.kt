package com.waroudi.poicountries.utils.extensions

import com.waroudi.poicountries.data.models.error.PoiError
import com.waroudi.poicountries.utils.ErrorConverter
import com.waroudi.poicountries.utils.ErrorUtils
import com.waroudi.poicountries.utils.sendToCrashlytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Attempts to collects the current flow. In case of failure, converts the error, reports it and throws it
 * @param converter an error converter. The default converter if none is given
 * @return a flow resulting from collecting the current flow
 * @throws PoiError
 */
fun <T> Flow<T>.convertError(converter: ErrorConverter = ErrorUtils.defaultConverter): Flow<T> {
    return flow {
        try {
            collect { value ->
                emit(value)
            }
        } catch (e: Exception) {
            val poiError = converter(e)
            poiError.sendToCrashlytics()
            throw poiError
        }
    }
}

/**
 * Attempts to collects the current flow, handles errors caught in the process
 * @param onError a lambda to handle errors, if any
 * @return a flow resulting from collecting the current flow
 */
fun <T> Flow<T>.doOnError(onError: (PoiError) -> Unit): Flow<T> {
    return flow {
        try {
            collect { value ->
                emit(value)
            }
        } catch (e: PoiError) {
            onError(e)
        }
    }
}