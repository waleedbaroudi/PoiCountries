package com.waroudi.poicountries.data.models.error

import com.waroudi.poicountries.utils.extensions.toSafeString

/**
 * A custom error type for PoiCountries
 * @param customMessage a custom message for readability or descriptiveness
 * @param cause the throwable causing this error
 */
sealed class PoiError(var customMessage: String?, cause: Throwable? = null): Exception(customMessage, cause) {
    // General Types
    object NetworkError: PoiError("Sorry, a network error occurred")
    object UnknownError : PoiError("Sorry, Something went wrong")
    data class OtherError(val errorMessage: String?, val errorCause: Throwable? = null) : PoiError(errorMessage, errorCause)
    // Case specific types
    object CountryNotFoundError: PoiError("Country not found")

    open fun safeMessage() = customMessage.toSafeString("No message")
}
