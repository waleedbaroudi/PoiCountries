package com.waroudi.poicountries.ui.home

import androidx.core.os.bundleOf
import com.waroudi.poicountries.R
import com.waroudi.poicountries.databinding.FragmentHomeBinding
import com.waroudi.poicountries.ui.base.BaseFragment
import com.waroudi.poicountries.ui.components.dialogs.InputDialog
import com.waroudi.poicountries.utils.Constants
import com.waroudi.poicountries.utils.extensions.navigate

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var countrySearchDialog: InputDialog? = null

    override fun setupView() {
    }

    override fun setupListeners() {
        super.setupListeners()
        binding.btnCountryList.setOnClickListener {
            navigate(R.id.action_home_to_countries)
        }
        binding.btnCountrySearch.setOnClickListener {
            showCountrySearchDialog()
        }
    }

    /**
     * Shows an input dialog for searching for a country by name
     */
    private fun showCountrySearchDialog() {
        if (countrySearchDialog == null)
            countrySearchDialog = InputDialog(getString(R.string.dialog_title_country_search), getString(R.string.btn_search), getString(R.string.hint_country_search), R.drawable.ic_search) { inputName ->
                navigate(R.id.action_home_to_details, bundleOf(Constants.KEY_SELECTED_COUNTRY_NAME to inputName))
            }

        countrySearchDialog?.show(childFragmentManager)
    }

}