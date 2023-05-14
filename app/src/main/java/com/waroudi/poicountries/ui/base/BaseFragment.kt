package com.waroudi.poicountries.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.waroudi.poicountries.data.models.error.PoiError
import com.waroudi.poicountries.data.models.state.UiModelState
import com.waroudi.poicountries.ui.main.MainActivity
import com.waroudi.poicountries.utils.extensions.createBindingInstance
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected lateinit var binding: VB
    private var _binding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        if (_binding == null) {
            _binding = createBindingInstance(inflater, container)
            binding = _binding!!
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleArgs()
        setupView()
        setupListeners()
        subscribesUI()
    }

    /**
     * For handling initial view state of the fragment
     */
    abstract fun setupView()

    /**
     * For reading and consuming navigation arguments
     */
    open fun handleArgs() {}

    /**
     * For setting any listeners (e.g. click listeners)
     */
    open fun setupListeners() {}

    /**
     * For observing ViewModels UiState members
     */
    open fun subscribesUI() {}

    /**
     * Observes state updates of a given [StateFlow]
     * @param stateFlow the state flow to be observed
     * @param loading an action for when the flow is in [UiModelState.Loading] state. By default this will show progress dialog
     * @param success an action for when the flow is in [UiModelState.Success] state
     * @param error an action for when the flow is in [UiModelState.Error] state. By default this will show error dialog
     * @param end and action for when the flow is in a final state (i.e. Success or Error)
     * @param ignoreLoading a flag to ignore loading state
     * @param ignoreError a flag to ignore error state
     * @param cancelOnEnd a flag to cancel the scope when a final state is reached (i.e. Success or Error)
     * @param hideLoadingOnEnd a flag to hide loading when a final state is reached (i.e. Success or Error)
     */
    fun <T> observeFlow(
        stateFlow: StateFlow<UiModelState<T>>,
        loading: (() -> Unit)? = { showProgress(true) },
        success: ((T) -> Unit)? = null,
        error: ((PoiError) -> Unit)? = { showDialogError(it) },
        end: (() -> Unit)? = null,
        ignoreLoading: Boolean = false,
        ignoreError: Boolean = false,
        cancelOnEnd: Boolean = false,
        hideLoadingOnEnd: Boolean = true
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val hideLoading = hideLoadingOnEnd && !ignoreLoading
            stateFlow.collect { result ->
                when (result) {
                    is UiModelState.Loading -> {
                        if (ignoreLoading.not()) loading?.invoke()
                    }
                    is UiModelState.Success<T> -> {
                        success?.invoke(result.data)
                        end?.invoke()
                        if (hideLoading) showProgress(false)
                        if (cancelOnEnd) cancel()
                    }
                    is UiModelState.Error -> {
                        val exc = result.exception
                        if (ignoreError.not()) error?.invoke(exc)
                        end?.invoke()
                        if (hideLoading) showProgress(false)
                        if (cancelOnEnd) cancel()
                    }
                    is UiModelState.None -> {
                        if (hideLoading) showProgress(false)
                    }
                }
            }
        }
    }

    /**
     * Queues for showing/hiding progress dialog
     * @param state true if queuing to show progress, false otherwise
     */
    private fun showProgress(state: Boolean) {
        if (isAdded) requireMainActivity().updateProgress(state)
    }

    /**
     * Shows a [com.waroudi.poicountries.ui.components.dialogs.ErrorDialog]. See linked documentation for further details
     */
    fun showDialogError(
        exception: PoiError = PoiError.UnknownError,
        buttonText: String? = null,
        forcePerformAction: Boolean = false,
        action: (() -> Unit)? = null
    ) {
        if (isAdded) requireMainActivity().showDialogError(
            exception = exception,
            btnText = buttonText,
            forcePerformAction,
            action = action
        )
    }

    /**
     * Requires activity as [MainActivity]
     * @return main activity
     */
    private fun requireMainActivity() = (requireActivity() as MainActivity)

}