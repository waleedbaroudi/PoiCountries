package com.waroudi.poicountries.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waroudi.poicountries.data.models.api.PoiResult
import com.waroudi.poicountries.data.models.error.PoiError
import com.waroudi.poicountries.data.models.state.UiModelState
import com.waroudi.poicountries.data.models.state.data
import com.waroudi.poicountries.data.models.state.succeeded
import com.waroudi.poicountries.utils.extensions.doOnError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    /**
     * A wrapper for handling flow collection
     * @param input the flow to be collected
     * @param mutableStateFlow a state flow to receive flow collection events (e.g. loading, success)
     * @param cached a flag to emit already fetched data, if it exists
     */
    protected fun <T> flowWrapper(
        input: Flow<PoiResult<T>>,
        mutableStateFlow: MutableStateFlow<UiModelState<T>>,
        cached: Boolean = false
    ) {
        if (cached.and(mutableStateFlow.value.succeeded)) {
            mutableStateFlow.value.data?.let { data ->
                mutableStateFlow.value = UiModelState.None
                mutableStateFlow.value = UiModelState.Success(data).copy()
            }
        } else {
            viewModelScope.launch {
                mutableStateFlow.value = UiModelState.Loading
                input.doOnError {
                    mutableStateFlow.value = UiModelState.Error(it)
                }.collect { result ->
                    val data = result.data
                    if (data != null) {
                        mutableStateFlow.value = UiModelState.Success(data)
                    } else {
                        val error = result.error ?: PoiError.UnknownError
                        mutableStateFlow.value = UiModelState.Error(error)
                    }
                }
            }
        }
    }
}