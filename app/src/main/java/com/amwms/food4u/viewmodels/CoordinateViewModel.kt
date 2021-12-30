package com.amwms.food4u.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CoordinateViewModel : ViewModel() {

    // Country to choose restaurants from
    private val _country = MutableLiveData<String>()
    val country: LiveData<String> = _country

    // Restaurant to search menu
    private val _restaurant = MutableLiveData<String>()
    val restaurant: LiveData<String> = _restaurant

    // temporary country during setting country value
    private val _tempCountry = MutableLiveData<String>()
    val tempCountry: LiveData<String> = _tempCountry

    // temporary restaurant during setting restaurant value
    private val _tempRestaurant = MutableLiveData<String>()
    val tempRestaurant: LiveData<String> = _tempRestaurant


    init {
        resetSettings()
    }

    private fun resetSettings() {
        _country.value = ""
        _restaurant.value = ""
    }

    // setters
    fun setCountry(country: String) {
        _country.value = country
    }

    fun setRestaurant(restaurant: String) {
        _restaurant.value = restaurant
    }

    fun setTempCountry(country: String) {
        _tempCountry.value = country
    }

    fun setTempRestaurant(restaurant: String) {
        _tempRestaurant.value = restaurant
    }

}