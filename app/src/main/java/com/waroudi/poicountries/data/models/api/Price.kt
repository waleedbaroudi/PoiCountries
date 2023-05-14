package com.waroudi.poicountries.data.models.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(val amount: Int, val currencyCode: String) : Parcelable {

    /**
     * Formats the amount with a thousands comma separator
     * @return formatted amount
     */
    fun getFormattedAmount(): String {
        return String.format("%,d", amount)
    }

    /**
     * Gets currency symbol from the currency code
     * @return currency symbol, or the code itself if not recognized
     */
    fun getCurrencySymbol(): String {
        return when(currencyCode) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            else -> currencyCode
        }
    }
}