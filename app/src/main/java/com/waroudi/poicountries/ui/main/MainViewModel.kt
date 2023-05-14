package com.waroudi.poicountries.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.waroudi.poicountries.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    private val _progressCounter = MutableLiveData(0)
    val progressCounter: LiveData<Int> = _progressCounter

    /**
     * Updates the progress queue counter, making sure it does not go below 0
     * @param add whether to add or subtract from the counter
     */
    fun updateProgress(add: Boolean) {
        _progressCounter.value?.let { oldValue ->
            _progressCounter.value = if (add) oldValue + 1 else (oldValue - 1).coerceAtLeast(0)
        }
    }
}