package com.waroudi.poicountries.utils.extensions

import com.google.gson.Gson
import retrofit2.Response

inline fun <reified T> Response<*>.convertErrorBody(): T? {
    val bodyJson = errorBody()?.charStream()?.readText()
    return try {
        Gson().fromJson(bodyJson, T::class.java)
    } catch (ignored: Exception) {
        print(ignored)
        null
    }
}