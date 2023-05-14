package com.waroudi.poicountries.data.models.state

import com.waroudi.poicountries.data.models.error.PoiError

/**
 * A state model for the UI
 */
sealed class UiModelState<out R> {
    data class Success<out T>(val data: T) : UiModelState<T>()
    data class Error(val exception: PoiError) : UiModelState<Nothing>()
    object Loading : UiModelState<Nothing>()
    object None : UiModelState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
            is None -> "Pending State"
        }
    }
}

/**
 * Check if the state is a success
 * @return true if the state is [UiModelState.Success] and its data is not null, false otherwise
 */
val UiModelState<*>.succeeded
    get() = this is UiModelState.Success && data != null

/**
 * Get data of the current state, if available
 * @return data of type [T], or null if not available
 */
val <T> UiModelState<T>.data: T?
    get() = (this as? UiModelState.Success)?.data