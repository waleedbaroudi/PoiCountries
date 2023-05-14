package com.waroudi.poicountries.ui.country_details

import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.data.models.state.UiModelState
import com.waroudi.poicountries.data.models.state.data
import com.waroudi.poicountries.data.repositories.CountriesRepository
import com.waroudi.poicountries.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountryDetailsViewModel(private val repository: CountriesRepository) : BaseViewModel() {
    private val _country = MutableStateFlow<UiModelState<Country>>(UiModelState.None)
    val country: StateFlow<UiModelState<Country>> get() = _country

    private val _countries = MutableStateFlow<UiModelState<List<Country>>>(UiModelState.None)
    val countries: StateFlow<UiModelState<List<Country>>> get() = _countries

    /**
     * Sets the country model to a given [Country]
     * @param country the country to be set
     */
    fun setCountry(country: Country) {
        _country.value = UiModelState.Success(country)
    }

    fun getAllCountries() {
        flowWrapper(repository.getAllCountries(), _countries, true)
    }

    fun getBorderNames(country: Country): List<String> {
        return _countries.value.data?.let { countries ->
            countries.filter { country.borders?.contains(it.cca3) ?: false }.map { it.name.common }
        } ?: listOf()
    }

}