package com.amwms.food4u.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amwms.food4u.database.Food4UDao
import com.amwms.food4u.database.entities.Dish

class CreateSetViewModel(private val food4uDao: Food4UDao): ViewModel() {
    // database functions
    fun allMenuItemsWithConstraints(allergenNames: List<String>, maxCalories: Int, minCalories: Int, countryId: Int, restaurantId: Int): List<Dish>
            = food4uDao.getDishesWithConstraintsList(allergenNames, maxCalories, minCalories, countryId, restaurantId)

    fun dishCalories(dishId: Int): Int = food4uDao.getDishIdEnergy(dishId)

    // functions
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



//    fun createDishSets(allergenNames: List<String>, maxCalories: Int, minCalories: Int,
//                       countryId: Int, restaurantId: Int,
//                       maxSetCount: Int, setSize: Int): MutableList<Pair<Int, List<Dish>>> {
//
//        val dishes: List<Dish>
//        val convertedDishes: List<Pair<Dish, Int>>
//
//
//        GlobalScope.launch(Dispatchers.IO) {
//            val _dishes = allMenuItemsWithConstraints(allergenNames, maxCalories, minCalories, countryId, restaurantId)
//            Log.d("create  set view model", "done")
//
//            .activity?.runOnUiThread() {
//                dishes = _dishes
//                convertedDishes = convertToDishesList(dishes)
//            }
//        }
//
////        val dishes = allMenuItemsWithConstraints(allergenNames, maxCalories, minCalories, countryId, restaurantId)
////
////        val convertedDishes = convertToDishesList(dishes)
//
//        val result = ArrayList<Pair<Int, List<Dish>>>()
//        for (i in 0 until maxSetCount) {
//            result.add(createDishSet(convertedDishes, setSize, maxCalories))
//        }
//
//        return result.toMutableList()
//    }
//
//    private fun convertToDishesList(dishes: List<Dish>): List<Pair<Dish, Int>> {
//        val result = ArrayList<Pair<Dish, Int>>()
//
//        for (i in 0 until dishes.size) {
//            val energy = dishCalories(dishes[i].id)
//            result.add(Pair(dishes[i], energy))
//        }
//
//        return result.toList()
//    }
//
//    private fun createDishSet(dishes: List<Pair<Dish, Int>>, setSize: Int, maxCalories: Int): Pair<Int, List<Dish>> {
//        var calCount = 0
//        var dishCount = 0
//        val shuffledList = dishes.shuffled()
//        val set = ArrayList<Dish>()
//
////        for (i in 0 until (shuffledList.size - setSize)) {
//            val calorieCountId = shuffledList[0].second
//            calCount += calorieCountId
//            set.add(shuffledList[0].first)
//
//            for (j in 1 until (shuffledList.size - setSize)) {
//                if (shuffledList[j].second + calorieCountId < maxCalories) {
//                    set.add(shuffledList[j].first)
//
//                    calCount += shuffledList[j].second
//                    dishCount++
//                }
//
//                if (dishCount == setSize) {
//                    break;
//                }
//            }
////        }
//
//        return Pair(calCount, set.toList())
//    }

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
