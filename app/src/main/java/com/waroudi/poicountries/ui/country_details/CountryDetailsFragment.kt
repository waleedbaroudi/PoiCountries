package com.waroudi.poicountries.ui.country_details

import com.waroudi.poicountries.R
import com.waroudi.poicountries.data.models.api.Country
import com.waroudi.poicountries.data.models.error.PoiError
import com.waroudi.poicountries.databinding.FragmentCountryDetailsBinding
import com.waroudi.poicountries.ui.base.BaseFragment
import com.waroudi.poicountries.utils.*
import com.waroudi.poicountries.utils.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CountryDetailsFragment : BaseFragment<FragmentCountryDetailsBinding>() {

    private val viewModel: CountryDetailsViewModel by viewModel()

    override fun setupView() {
    }

    override fun setupListeners() {
        setupCardBehavior()
    }

    private fun setupCardBehavior() {
        with(binding) {
            btnDetails.setOnClickListener {
                val isFront = cardCountry.rotationY == 0f
                val inView = if (isFront) layoutBack else layoutFront
                val outView = if (isFront) layoutFront else layoutBack
                outView.animate().alpha(0f).setDuration(150).start()
                cardCountry.animate().rotationY(if (isFront) 180f else 0f).setDuration(400).start()
                inView.animate().alpha(1f).setStartDelay(200).setDuration(150).start()
                btnDetails.text = getString(if (isFront) R.string.btn_hide_details else R.string.btn_show_details)
            }
        }
    }

    override fun subscribesUI() {
        with(viewModel) {
            observeFlow(country,
                success = { handleCountry(it) },
                error = { handleCountryError(it) })

            observeFlow(countries,
                success = { handleCountries(it) },
                error = { handleCountryError(it) })

            getAllCountries()
        }
    }

    private fun handleCountries(countries: List<Country>) {
        arguments?.let { args ->
            args.getString(Constants.KEY_SELECTED_COUNTRY_NAME)?.let { name ->
                countries.firstOrNull { it.name.common.equals(name, true) }?.let {
                    viewModel.setCountry(it)
                } ?: handleCountryError(PoiError.CountryNotFoundError)
            } ?: handleCountryError(PoiError.CountryNotFoundError)
        }
    }

    /**
     * Applies retrieved Country to UI
     * @param country retrieved country
     */
    private fun handleCountry(country: Country) {
        with(binding) {
            groupElements.toVisible()
            tvName.text = country.name.common
            tvCapital.text = country.getCapital()
            tvSubregion.text = country.subregion
            tvPopulation.text = country.population.formatAbbreviated()
            tvArea.text = country.getFormattedArea()
            tvLanguages.text = country.getListOfLanguages().formatBulleted()
            viewModel.getBorderNames(country).let {
                tvBorders.text = if (it.isEmpty()) "No countries border ${country.name.common}" else it.formatBulleted()
            }
            imgCover.setGlideImage(country.getFlagUrl(), R.drawable.ic_europe)
        }
    }

    /**
     * Handles country retrieval error
     * @param error country retrieval error
     */
    private fun handleCountryError(error: PoiError? = null) {
        showDialogError(error ?: PoiError.UnknownError, forcePerformAction = true) { navigateBack() }
    }
}