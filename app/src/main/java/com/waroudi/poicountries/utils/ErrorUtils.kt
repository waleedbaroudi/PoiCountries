package com.waroudi.poicountries.utils

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.waroudi.poicountries.data.models.error.PoiError
import com.waroudi.poicountries.utils.extensions.notNull
import retrofit2.HttpException

/**
 * A lambda that converts an [Exception] into a [PoiError]
 */
typealias ErrorConverter = (error: Exception) -> PoiError

object ErrorUtils {
    /**
     * Default [ErrorConverter] for general errors
     */
    val defaultConverter: ErrorConverter = { error ->
        when {
            error is HttpException -> {
                PoiError.NetworkError
            }
            error.message.notNull() -> PoiError.OtherError(error.message, error)
            else -> PoiError.UnknownError
        }
    }
}

/**
 * Extension for adding conversion rules to the current converter
 */
fun ErrorConverter.extend(converter: ErrorConverter): ErrorConverter {
    return { error ->
        try {
            converter(error)
        } catch (exc: Exception) {
            invoke(exc)
        }
    }
}

/**
 * Reports this exception to Crashlytics
 */
fun Exception.sendToCrashlytics() {
    Firebase.crashlytics.recordException(this)
}