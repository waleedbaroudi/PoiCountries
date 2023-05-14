package com.waroudi.poicountries.ui.countries

import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.data.models.state.UiModelState
import com.waroudi.poicountries.data.models.state.data
import com.waroudi.poicountries.data.repositories.CountriesRepository
import com.waroudi.poicountries.ui.base.BaseViewModel
import com.waroudi.poicountries.ui.components.dialogs.Sorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountriesViewModel(private val repository: CountriesRepository) : BaseViewModel() {

    private val _countryList = MutableStateFlow<UiModelState<List<Country>>>(UiModelState.None)
    val countryList: StateFlow<UiModelState<List<Country>>> get() = _countryList

    fun getAllCountries() {
        flowWrapper(repository.getAllCountries(), _countryList, true)
    }

    fun getSetOfLanguages(): List<String> {
        val setOfLanguages = mutableSetOf<String>()
        _countryList.value.data?.map { it.getListOfLanguages() }?.forEach { set ->
            setOfLanguages.addAll(set)
        }
        return setOfLanguages.toList()
    }

    fun getSetOfSubregions(): List<String> {
        return _countryList.value.data?.map { it.subregion }?.toSet()?.toList() ?: listOf()
    }

    fun setRecentSorting(sorting: Sorting) {
        repository.setRecentSorting(sorting)
    }

    fun getRecentSorting(): Sorting? {
        return repository.getRecentSorting()
    }

}