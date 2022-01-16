package com.amwms.food4u.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Country
import com.amwms.food4u.database.entities.Restaurant
import kotlinx.coroutines.flow.Flow

class CountryRestaurantViewModel(private val food4uDao: Food4UDao): ViewModel() {
    // database functions
    fun allCountries(): Flow<List<Country>> = food4uDao.getAllCountries()

    fun allRestaurants(): Flow<List<Restaurant>> = food4uDao.getAllRestaurants()

    // fragment coordinating functions and variables
    // temporary country during setting country value
    private val _tempCountryName = MutableLiveData<String>()
    val tempCountryName: LiveData<String> = _tempCountryName

    private val _tempCountryId = MutableLiveData<Int>(-1)
    val tempCountryId: LiveData<Int> = _tempCountryId

    // temporary restaurant during setting restaurant value
    private val _tempRestaurantName = MutableLiveData<String>()
    val tempRestaurantName: LiveData<String> = _tempRestaurantName

    private val _tempRestaurantId = MutableLiveData<Int>(-1)
    val tempRestaurantId: LiveData<Int> = _tempRestaurantId

    init {
        resetSettings()
    }

    private fun resetSettings() {
        _tempCountryId.value = -1
        _tempCountryName.value = ""
        _tempRestaurantId.value = -1
        _tempRestaurantName.value = ""
    }

    fun setTempCountry(country: Country) {
        _tempCountryName.value = country.name
        _tempCountryId.value = country.id
    }

    fun setTempRestaurant(restaurant: Restaurant) {
        _tempRestaurantName.value = restaurant.name
        _tempRestaurantId.value = restaurant.id
    }
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
