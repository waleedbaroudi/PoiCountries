package com.waroudi.poicountries.data.models.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.waroudi.poicountries.utils.extensions.toSafeString
import kotlinx.parcelize.Parcelize

/**
 * Country model
 */
@Parcelize
data class Country(
    @SerializedName("name") val name: NameDetails,
    @SerializedName("capital") val capital: List<String>,
    @SerializedName("subregion") val subregion: String,
    @SerializedName("languages") val languages: MutableMap<String,String>,
    @SerializedName("flags") val flags: MutableMap<String,String>,
    @SerializedName("population") val population: Int,
    @SerializedName("area") val area: Float,
    @SerializedName("borders") val borders: List<String>?,
    @SerializedName("cca3") val cca3: String
): Parcelable {

    /**
     * Gets the capital of the country
     * @return a capital's name
     */
    fun getCapital() = capital.firstOrNull().toSafeString()

    fun getFlagUrl(): String? {
        return flags["png"]
    }

    /**
     * Extracts and returns the list of languages as Strings
     * @return list of languages
     */
    fun getListOfLanguages() = languages.values.toList()

    /**
     * Formats the area in a string by adding area unit
     * @return Formatted area string
     */
    fun getFormattedArea() = "$area km\u00B2"
}

@Parcelize
data class NameDetails(val common: String): Parcelable
