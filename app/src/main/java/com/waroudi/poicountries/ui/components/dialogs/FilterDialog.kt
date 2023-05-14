package com.waroudi.poicountries.ui.components.dialogs

import android.widget.ArrayAdapter
import com.waroudi.poicountries.R
import com.waroudi.poicountries.databinding.DialogFilterBinding
import com.waroudi.poicountries.ui.base.BaseDialogFragment

/**
 * Custom dialog for taking user filtering input
 * @param filterCallback the callback for the user selected options
 */
class FilterDialog(
    private val languages: List<String>,
    private val subregions: List<String>,
    private val filterCallback: (FilterFeature, featureValue: String) -> Unit,
    private val clearFilterCallback: () -> Unit
) : BaseDialogFragment<DialogFilterBinding>() {

    private lateinit var filterValueAdapter: ArrayAdapter<String>

    override fun setupView() {
        // Setup filter value spinner
        filterValueAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner, languages.toList())
        binding.spinnerFilter.adapter = filterValueAdapter
    }

    override fun setupListeners() {
        with(binding) {
            // Setup button action
            btnOkay.setOnClickListener {
                val selectedFeature = getSelectedFeature()
                val selectedValue = getSelectedValue()
                filterCallback(selectedFeature, selectedValue)
                safeDismiss()
            }
            btnClear.setOnClickListener {
                clearFilterCallback()
                safeDismiss()
            }
            toggleFeature.addOnButtonCheckedListener { _, _, isChecked -> if (isChecked) handleSelectedFeature() }
        }
    }

    private fun handleSelectedFeature() {
        val filterValues = if (binding.btnLanguage.isChecked) languages else subregions
        filterValueAdapter.apply {
            clear()
            addAll(filterValues.toList())
            notifyDataSetChanged()
        }
    }

    private fun getSelectedFeature() =
        if (binding.toggleFeature.checkedButtonId == R.id.btn_subregion) FilterFeature.SUBREGION else FilterFeature.LANGUAGE

    private fun getSelectedValue() =
        binding.spinnerFilter.selectedItem.toString()

}

enum class FilterFeature {
    LANGUAGE,
    SUBREGION
}