package com.waroudi.poicountries.data.models.error

import com.google.gson.annotations.SerializedName
import com.waroudi.poicountries.utils.extensions.toSafeString

data class PoiServiceError(@SerializedName("message") val errorMessage: String?) : PoiError(errorMessage, null) {
    override fun safeMessage(): String {
        return errorMessage.toSafeString("No message")
    }
}