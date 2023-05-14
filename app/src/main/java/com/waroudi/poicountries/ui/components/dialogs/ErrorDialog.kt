package com.waroudi.poicountries.ui.components.dialogs

import android.content.DialogInterface
import com.waroudi.poicountries.R
import com.waroudi.poicountries.databinding.DialogErrorBinding
import com.waroudi.poicountries.ui.base.BaseDialogFragment

/**
 * Custom dialog for displaying errors
 * @param message the error message to be shown
 * @param buttonText the label of the button
 * @param forcePerformAction if true, the given action will always be performed when the dialog is dismissed. Otherwise the action will only be invoked from
 * the button click
 * @param action the action to be performed from this dialog
 */
class ErrorDialog(
    private val message: String,
    private val buttonText: String? = null,
    private val forcePerformAction: Boolean = false,
    private val action: (() -> Unit)? = null
) : BaseDialogFragment<DialogErrorBinding>() {

    override fun setupView() {
        binding.lblDescription.text = message
        buttonText?.let {
            binding.btnOkay.text = it
        }
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_round_corner_dialog)
    }

    override fun setupListeners() {
        binding.btnOkay.setOnClickListener {
            if (forcePerformAction.not())
                action?.invoke()
            safeDismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (forcePerformAction)
            action?.invoke()
    }
}