package com.waroudi.poicountries.ui.countries

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.waroudi.poicountries.R
import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.databinding.LayoutCountryCellBinding
import com.waroudi.poicountries.ui.components.dialogs.FilterFeature
import com.waroudi.poicountries.ui.components.dialogs.Sorting
import com.waroudi.poicountries.ui.components.dialogs.SortingFeature
import com.waroudi.poicountries.ui.components.dialogs.SortingOrder
import com.waroudi.poicountries.utils.extensions.formatAbbreviated
import com.waroudi.poicountries.utils.extensions.setGlideImage
import com.waroudi.poicountries.utils.extensions.sortByName
import com.waroudi.poicountries.utils.extensions.sortByPopulation

class CountriesAdapter(
    private val countries: List<Country>,
    private val countryItemListener: CountryItemListener
) : Adapter<CountryViewHolder>() {

    private var filteredCountries = countries.toList()

    private var searchedText = ""

    private var sorting: Sorting? = null

    private var filterFeature: FilterFeature? = null
    private var filterValue = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutCountryCellBinding.inflate(inflater, parent, false)
        return CountryViewHolder(binding, countryItemListener)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(filteredCountries[position])
    }

    override fun getItemCount() = filteredCountries.size


    @SuppressLint("NotifyDataSetChanged")
    fun search(text: String) {
        searchedText = text
        val searched = countries.filter { it.name.common.contains(text, true) }
        val sorted = sorting?.let { sorting ->
            val isDescending = sorting.order == SortingOrder.DESCENDING
            when (sorting.feature) {
                SortingFeature.NAME -> searched.sortByName(isDescending)
                SortingFeature.POPULATION -> searched.sortByPopulation(isDescending)
            }
        } ?: searched
        val filtered = filterFeature?.let { feature ->
            when (feature) {
                FilterFeature.LANGUAGE -> sorted.filter { it.getListOfLanguages().contains(filterValue) }
                FilterFeature.SUBREGION -> sorted.filter { it.subregion.equals(filterValue, true) }
            }
        } ?: sorted
        filteredCountries = filtered
        notifyDataSetChanged()
    }

    fun sort(sorting: Sorting) {
        this.sorting = sorting
        search(searchedText)
    }

    fun filter(feature: FilterFeature, value: String) {
        filterFeature = feature
        filterValue = value
        search(searchedText)
    }

    fun clearFilter() {
        filterFeature = null
        search(searchedText)
    }
}

class CountryViewHolder(
    private val binding: LayoutCountryCellBinding,
    private val countryItemListener: CountryItemListener
) : ViewHolder(binding.root) {
    fun bind(country: Country) {
        with(binding) {
            tvName.text = country.name.common
            imgThumbnail.setGlideImage(country.getFlagUrl(), R.drawable.ic_europe)
            tvSubregion.text = country.subregion
            tvPopulation.text = country.population.formatAbbreviated()
            root.setOnClickListener { countryItemListener.onCountryClicked(country) }
        }
    }
}

interface CountryItemListener {
    fun onCountryClicked(country: Country)
}