package com.waroudi.poicountries.ui.countries

import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.waroudi.poicountries.R
import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.databinding.FragmentCountriesBinding
import com.waroudi.poicountries.ui.base.BaseFragment
import com.waroudi.poicountries.ui.components.dialogs.FilterDialog
import com.waroudi.poicountries.ui.components.dialogs.SortingDialog
import com.waroudi.poicountries.utils.Constants
import com.waroudi.poicountries.utils.extensions.navigate
import com.waroudi.poicountries.utils.extensions.navigateBack
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountriesFragment : BaseFragment<FragmentCountriesBinding>(), CountryItemListener {

    private val viewModel: CountriesViewModel by viewModel()

    private lateinit var countriesAdapter: CountriesAdapter
    private var sortDialog: SortingDialog? = null
    private var filterDialog: FilterDialog? = null

    override fun setupView() {
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.editSearch.doOnTextChanged { text, _, _, _ ->
            countriesAdapter.search(text.toString())
        }

        binding.btnSort.setOnClickListener {
            showSortDialog()
        }

        binding.btnFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val languages = viewModel.getSetOfLanguages()
        val subregions = viewModel.getSetOfSubregions()
        print(languages)
        if (filterDialog == null)
            filterDialog = FilterDialog(
                languages,
                subregions,
                { feature, value -> countriesAdapter.filter(feature, value) },
                { countriesAdapter.clearFilter() }
            )
        filterDialog?.show(childFragmentManager)
    }

    private fun showSortDialog() {
        if (sortDialog == null)
            sortDialog = SortingDialog(viewModel.getRecentSorting()) { sorting ->
                viewModel.setRecentSorting(sorting)
                countriesAdapter.sort(sorting)
            }

        sortDialog?.show(childFragmentManager)
    }

    override fun subscribesUI() {
        with(viewModel) {
            observeFlow(countryList,
                success = { handleCountries(it) },
                error = {
                    showDialogError(it, forcePerformAction = true) { navigateBack() }
                })
            getAllCountries()
        }

    }

    /**
     * Handles the retrieved list of countries, sets up the country recycler view
     * @param countries list of retrieved countries
     */
    private fun handleCountries(countries: List<Country>) {
        countriesAdapter = CountriesAdapter(countries, this)
        viewModel.getRecentSorting()?.let { countriesAdapter.sort(it) }
        val manager = GridLayoutManager(requireContext(), 2)
        binding.recyclerCountries.apply {
            layoutManager = manager
            adapter = countriesAdapter
        }
    }

    override fun onCountryClicked(country: Country) {
        val args = bundleOf(Constants.KEY_SELECTED_COUNTRY_NAME to country.name.common)
        navigate(R.id.action_countries_to_details, args)
    }

}