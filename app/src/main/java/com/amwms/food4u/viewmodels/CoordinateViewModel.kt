package com.amwms.food4u.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CoordinateViewModel : ViewModel() {

    // Country to choose restaurants from
    private val _countryName = MutableLiveData<String>()
    val countryName: LiveData<String> = _countryName
    private val _countryId = MutableLiveData<Int>()
    val countryId: LiveData<Int> = _countryId

    // Restaurant to search menu
    private val _restaurantName = MutableLiveData<String>()
    val restaurantName: LiveData<String> = _restaurantName
    private val _restaurantId = MutableLiveData<Int>()
    val restaurantId: LiveData<Int> = _restaurantId

    init {
        resetSettings()
    }

    fun countryNotSet(): Boolean {
        return countryName.value.equals("")
    }

    fun restaurantNotSet(): Boolean {
        return restaurantName.value.equals("")
    }

    private fun resetSettings() {
        _countryId.value = -1
        _restaurantId.value = -1
        _countryName.value = ""
        _restaurantName.value = ""
    }

    // setters
    fun setCountry(countryId: Int?, countryName: String) {
        _countryId.value = countryId
        _countryName.value = countryName
    }

    fun setRestaurant(restaurantId: Int?, restaurantName: String) {
        _restaurantId.value = restaurantId
        _restaurantName.value = restaurantName
    }

}