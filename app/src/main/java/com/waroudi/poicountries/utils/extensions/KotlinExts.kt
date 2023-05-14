package com.waroudi.poicountries.utils.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

/**
 * Returns a safe string representation of this object
 * @param altText returned text if this object is null
 * @param checkBlank a flag to return [altText] if the String representation of this object is blank
 * @return String representation of the object
 */
fun Any?.toSafeString(altText: String = "Unknown", checkBlank: Boolean = false): String {
    return this?.toString().takeIf { if (checkBlank) it.isNullOrBlank().not() else it.notNull() } ?: altText
}

fun Any?.notNull() = isNull().not()

fun Any?.isNull() = this == null

// Bundle
/**
 * OS version compatible function to get [Parcelable] from a [Bundle]
 * @param key the key of the requested parcelable
 * @return the parcelable if it exists, null otherwise
 */
inline fun <reified T : Parcelable?> Bundle.getSafeParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelable<T>(key)
    }
}

/**
 * Returns the number in an abbreviated format, with the appropriate abbreviation letter (e.g. k for thousands)
 */
fun Number.formatAbbreviated(): String {
    val suffixes = listOf("", "k", "m", "b", "t")
    var value = toDouble()
    var suffixIndex = 0
    while (value >= 1000 && suffixIndex < suffixes.lastIndex) {
        value /= 1000
        suffixIndex++
    }
    return "%.${if (value < 10) 1 else 0}f%s".format(value, suffixes[suffixIndex])
}

/**
 * Formats strings in a bulleted list
 * @return the bulleted list as a formatted string
 */
fun List<String>.formatBulleted(): String {
    val bulleted = this.map { "- $it" }
    return bulleted.joinToString("\n")
}