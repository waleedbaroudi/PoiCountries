package com.waroudi.poicountries.ui.components.dialogs

import androidx.annotation.DrawableRes
import androidx.core.widget.addTextChangedListener
import com.waroudi.poicountries.databinding.DialogInputBinding
import com.waroudi.poicountries.ui.base.BaseDialogFragment
import com.waroudi.poicountries.utils.extensions.hideSoftKeyboard
import com.waroudi.poicountries.utils.extensions.toGone
import com.waroudi.poicountries.utils.extensions.toVisible

/**
 * Custom dialog for taking user input
 * @param title the input title
 * @param buttonTitle the label of the button
 * @param inputHint the hint shown on the input field
 * @param icon an optional icon resource
 * @param buttonAction the action to be performed with the given input
 */
class InputDialog
    (
    private val title: String,
    private val buttonTitle: String,
    private val inputHint: String,
    @DrawableRes private val icon: Int? = null,
    private val buttonAction: (input: String) -> Unit
) : BaseDialogFragment<DialogInputBinding>() {

    override fun setupView() {
        with(binding) {
            tvTitle.text = title
            icon?.let {
                imgIcon.setImageResource(it)
                imgIcon.toVisible()
            } ?: imgIcon.toGone()
            btnOkay.text = buttonTitle
            editInput.hint = inputHint
        }
    }

    override fun setupListeners() {
        with(binding) {
            // Setup button action
            btnOkay.setOnClickListener {
                root.hideSoftKeyboard()
                val input = editInput.text.toString()
                buttonAction(input)
                safeDismiss()
            }
            editInput.addTextChangedListener {
                it?.toString()?.let { text ->
                    btnOkay.isEnabled = text.isBlank().not()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editInput.setText("")
    }
}