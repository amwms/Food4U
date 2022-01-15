package com.amwms.food4u.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Dish
import kotlinx.coroutines.flow.Flow

class MenuViewModel(private val food4uDao: Food4UDao): ViewModel() {

    fun allDishes(): Flow<List<Dish>> = food4uDao.getAllDishes()

    fun allDishesContainingString(name: String, countryId: Int, restaurantId: Int): Flow<List<Dish>>
    = food4uDao.getDishesContaining("%$name%", countryId, restaurantId)

    fun allDishesWithConstraints(allergenNames: List<String>, maxCalories: Int, minCalories: Int, countryId: Int, restaurantId: Int): Flow<List<Dish>>
            = food4uDao.getDishesWithConstraints(allergenNames, maxCalories, minCalories, countryId, restaurantId)

    fun getNumberOfAllergens(): Int = food4uDao.getNumberOfAllergens()

    fun allAllergensNames(): List<String> = food4uDao.getAllAllergensNames()
}

class MenuViewModelFactory(
    private val food4uDao: Food4UDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuViewModel(food4uDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
