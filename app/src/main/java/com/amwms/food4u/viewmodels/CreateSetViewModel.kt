package com.amwms.food4u.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.database.entities.DishSet
import com.amwms.food4u.database.entities.FavoriteSet

class CreateSetViewModel(private val food4uDao: Food4UDao): ViewModel() {
    // database functions
    fun allMenuItemsWithConstraints(allergenNames: List<String>, maxCalories: Int, minCalories: Int, countryId: Int, restaurantId: Int): List<Dish>
            = food4uDao.getDishesWithConstraintsList(allergenNames, maxCalories, minCalories, countryId, restaurantId)

    fun dishCalories(dishId: Int): Int = food4uDao.getDishIdEnergy(dishId)

    fun addNewDishToSet(data: DishSet) = food4uDao.insertDishIntoSet(data)

    fun addNewSet(set: FavoriteSet) = food4uDao.insertNewSet(set)

    fun numberOfSets(): Int = food4uDao.getSetCount()

    fun dishExists(dishId: Int): Int = food4uDao.getDishIdExists(dishId)

    // fragment coordinating functions and variables
    private var setCount: Int = 0

    // current chosen dish set list
    private val _dishSetList = MutableLiveData<List<Dish>>()
    val dishSetList: LiveData<List<Dish>> = _dishSetList

    // calories of current chosen set
    private val _caloriesInSet = MutableLiveData<Int>()
    val caloriesInSet: LiveData<Int> = _caloriesInSet

    init {
        resetSettings()
    }

    private fun resetSettings() {
        setCount = 0
    }

    // setters
    fun setDishSetList(dishList: List<Dish>) {
        _dishSetList.value = dishList
    }

    fun setCalorieCount(calorieCount: Int) {
        _caloriesInSet.value = calorieCount
    }

}

class CreateSetViewModelFactory(
    private val food4uDao: Food4UDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateSetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateSetViewModel(food4uDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
