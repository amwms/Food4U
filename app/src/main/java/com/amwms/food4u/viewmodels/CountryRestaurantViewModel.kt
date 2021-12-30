package com.amwms.food4u.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Country
import com.amwms.food4u.database.entities.Restaurant
import kotlinx.coroutines.flow.Flow

class CountryRestaurantViewModel(private val food4uDao: Food4UDao): ViewModel() {

    fun allCountries(): Flow<List<Country>> = food4uDao.getAllCountries()

    fun allRestaurants(): Flow<List<Restaurant>> = food4uDao.getAllRestaurants()
}

class CountryRestaurantViewModelFactory(
    private val food4uDao: Food4UDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryRestaurantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountryRestaurantViewModel(food4uDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
