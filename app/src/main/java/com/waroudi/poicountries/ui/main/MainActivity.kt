package com.waroudi.poicountries.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.waroudi.poicountries.data.models.error.PoiError
import com.waroudi.poicountries.databinding.ActivityMainBinding
import com.waroudi.poicountries.databinding.LayoutProgressDialogBinding
import com.waroudi.poicountries.ui.components.dialogs.ErrorDialog
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeUI()
    }

    private fun subscribeUI() {
        // Observe progress queue. when queue is 0, hide progress.
        viewModel.progressCounter.observe(this) {
            if (it == 0) hideProgress()
            else showProgress()
        }
    }

    /**
     * Update progress queue
     * @param show whether to queue showing or hiding the progress
     */
    fun updateProgress(show: Boolean) {
        viewModel.updateProgress(show)
    }

    private fun isProgressShowing(): Boolean = progressDialog?.isShowing == true

    /**
     * Instantiates the progress dialog (if not already), and shows it if it's not visible
     */
    private fun showProgress() {
        if (isProgressShowing().not()) {
            if (progressDialog == null) progressDialog = createProgressLoading()
            progressDialog?.show()
        }
    }

    /**
     * Hides progress dialog
     */
    private fun hideProgress() = progressDialog?.dismiss()

    /**
     * Creates a progress dialog instance
     * @return a progress dialog
     */
    private fun createProgressLoading(): AlertDialog {
        val binding = LayoutProgressDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .setCancelable(false)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    /**
     * Shows an [ErrorDialog]. See linked documentation for further details
     * @param exception the error to show the dialog for
     */
    fun showDialogError(
        exception: PoiError,
        btnText: String?,
        forcePerformAction: Boolean = false,
        action: (() -> Unit)?
    ) {
        ErrorDialog(
            message = exception.safeMessage(),
            buttonText = btnText,
            forcePerformAction,
            action = action
        ).show(supportFragmentManager)
    }
}