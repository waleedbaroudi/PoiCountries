package com.waroudi.poicountries.ui.components.dialogs

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.core.widget.addTextChangedListener
import com.waroudi.poicountries.R
import com.waroudi.poicountries.databinding.DialogInputBinding
import com.waroudi.poicountries.databinding.DialogSortBinding
import com.waroudi.poicountries.ui.base.BaseDialogFragment
import com.waroudi.poicountries.utils.extensions.hideSoftKeyboard
import com.waroudi.poicountries.utils.extensions.toGone
import com.waroudi.poicountries.utils.extensions.toVisible
import kotlinx.parcelize.Parcelize

/**
 * Custom dialog for taking user sorting input
 * @param sortCallback the callback for the user selected options
 */
class SortingDialog(private val initialSorting: Sorting? = null, private val sortCallback: (Sorting) -> Unit) : BaseDialogFragment<DialogSortBinding>() {

    override fun setupView() {
        initialSorting?.let {
            binding.toggleFeature.check(if (it.feature == SortingFeature.NAME) R.id.btn_name else R.id.btn_population)
            binding.toggleOrder.check(if (it.order == SortingOrder.DESCENDING) R.id.btn_descending else R.id.btn_ascending)
        }
    }

    override fun setupListeners() {
        with(binding) {
            // Setup button action
            btnOkay.setOnClickListener {
                val selectedFeature = getSelectedFeature()
                val selectedOrder = getSelectedOrder()
                sortCallback(Sorting(selectedFeature, selectedOrder))
                safeDismiss()
            }

        }
    }

    private fun getSelectedFeature() =
        if (binding.toggleFeature.checkedButtonId == R.id.btn_population) SortingFeature.POPULATION else SortingFeature.NAME

    private fun getSelectedOrder() =
        if (binding.toggleOrder.checkedButtonId == R.id.btn_ascending) SortingOrder.ASCENDING else SortingOrder.DESCENDING

}

enum class SortingFeature {
    NAME,
    POPULATION
}

enum class SortingOrder {
    ASCENDING,
    DESCENDING
}

@Parcelize
data class Sorting(val feature: SortingFeature, val order: SortingOrder) : Parcelable