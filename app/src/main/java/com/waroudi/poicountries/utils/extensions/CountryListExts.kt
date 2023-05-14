package com.waroudi.poicountries.utils.extensions

import com.waroudi.poicountries.data.models.api.Country

fun List<Country>.sortByName(isDescending: Boolean): List<Country> {
    return if (isDescending) sortedByDescending { it.name.common } else sortedBy { it.name.common }
}

fun List<Country>.sortByPopulation(isDescending: Boolean): List<Country> {
    return if (isDescending) sortedByDescending { it.population } else sortedBy { it.population }
}