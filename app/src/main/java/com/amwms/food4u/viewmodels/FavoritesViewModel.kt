package com.amwms.food4u.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.database.entities.DishSet
import com.amwms.food4u.database.entities.FavoriteSet
import kotlinx.coroutines.flow.Flow

class FavoritesViewModel(private val food4uDao: Food4UDao): ViewModel() {

    fun allFavoriteSets(userId: String): Flow<List<FavoriteSet>> = food4uDao.getAllFavoriteSets(userId)

    fun allDishesInSet(setId: Int): Flow<List<Dish>> = food4uDao.getAllDishesInSet(setId)

    fun calorieSumInSet(setId: Int): Int = food4uDao.getCalorieSumInSet(setId)

    fun getFavoriteSetById(setId: Int): FavoriteSet = food4uDao.getFavoriteSetById(setId)

    fun deleteFavoriteSet(set: FavoriteSet) = food4uDao.deleteSet(set)

    fun allDishesInSetList(setId: Int): List<Dish> = food4uDao.getAllDishesInSetList(setId)

    fun getDishSetById(dishId: Int, setId: Int): DishSet = food4uDao.getDishSetById(dishId, setId)

    fun deleteDishFromSet(dish: DishSet) = food4uDao.deleteDishInSet(dish)

}

class FavoritesViewModelFactory(
    private val food4uDao: Food4UDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(food4uDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
