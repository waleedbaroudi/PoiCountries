package com.waroudi.poicountries.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waroudi.poicountries.ui.components.dialogs.Sorting
import com.waroudi.poicountries.ui.components.dialogs.SortingFeature
import com.waroudi.poicountries.ui.components.dialogs.SortingOrder
import com.waroudi.poicountries.utils.get
import com.waroudi.poicountries.utils.getString
import com.waroudi.poicountries.utils.putBoolean
import com.waroudi.poicountries.utils.putString
import kotlinx.coroutines.runBlocking

/**
 * Manages data stored locally
 * @param store the [DataStore] that contains local data
 */
class PoiLocalStorage(private val store: DataStore<Preferences>) {

    /**
     * Retrieves the last sorting option selected by the user
     * @return Sorting option
     */
    fun setRecentSorting(sorting: Sorting) {
        runBlocking {
            val sortingString = "${sorting.feature},${sorting.order}"
            store.putString(KEY_RECENT_SORTING, sortingString)
        }
    }

    /**
     * Retrieves the last sorting option selected by the user
     * @return Sorting option
     */
    fun getRecentSorting(): Sorting? {
        return runBlocking {
            store.getString(KEY_RECENT_SORTING)?.split(",")?.takeIf { it.count() == 2 }?.let {
                Sorting(SortingFeature.valueOf(it[0]), SortingOrder.valueOf(it[1]))
            }
        }
    }


    companion object {
        // Keys
        const val KEY_RECENT_SORTING = "FAVORITE_LIST"
    }
}