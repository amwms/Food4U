package com.amwms.food4u.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Calories
import kotlinx.coroutines.flow.Flow

class CaloriesViewModel(private val food4uDao: Food4UDao): ViewModel() {

    fun allDishIdCalories(dishId: Int): Flow<List<Calories>>
            = food4uDao.getDishIdCalories(dishId)
}

class CaloriesViewModelFactory(
    private val food4uDao: Food4UDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaloriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CaloriesViewModel(food4uDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
