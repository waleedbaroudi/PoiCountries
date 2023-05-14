package com.waroudi.poicountries.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// DataStore extensions for storing data
suspend fun DataStore<Preferences>.putString(key: String, value: String) = put(stringPreferencesKey(key), value)
suspend fun DataStore<Preferences>.putInt(key: String, value: Int) = put(intPreferencesKey(key), value)
suspend fun DataStore<Preferences>.putBoolean(key: String, value: Boolean) = put(booleanPreferencesKey(key), value)
suspend fun DataStore<Preferences>.putLong(key: String, value: Long) = put(longPreferencesKey(key), value)
suspend fun DataStore<Preferences>.putFloat(key: String, value: Float) = put(floatPreferencesKey(key), value)
suspend fun DataStore<Preferences>.putDouble(key: String, value: Double) = put(doublePreferencesKey(key), value)
suspend fun DataStore<Preferences>.putStringSet(key: String, value: Set<String>) = put(stringSetPreferencesKey(key), value)

// DataStore extensions for retrieving data
suspend fun DataStore<Preferences>.getString(key: String) = get(stringPreferencesKey(key))
suspend fun DataStore<Preferences>.getInt(key: String) = get(intPreferencesKey(key))
suspend fun DataStore<Preferences>.getBoolean(key: String) = get(booleanPreferencesKey(key))
suspend fun DataStore<Preferences>.getLong(key: String) = get(longPreferencesKey(key))
suspend fun DataStore<Preferences>.getFloat(key: String) = get(floatPreferencesKey(key))
suspend fun DataStore<Preferences>.getDouble(key: String) = get(doublePreferencesKey(key))
suspend fun DataStore<Preferences>.getStringSet(key: String) = get(stringSetPreferencesKey(key))

/**
 * Clears all data in the current DataStore
 */
fun DataStore<Preferences>.clear() =
    runBlocking {
        edit { it.clear() }
        Unit
    }

private suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { store -> store[key] = value }
}

suspend inline fun <reified T> DataStore<Preferences>.get(key: Preferences.Key<T>) = data.first()[key]