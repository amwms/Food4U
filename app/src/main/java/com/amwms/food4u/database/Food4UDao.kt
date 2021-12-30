package com.amwms.food4u.database

import androidx.room.Dao
import androidx.room.Query
import com.amwms.food4u.database.entities.Allergen
import com.amwms.food4u.database.entities.Country
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.database.entities.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface Food4UDao {

    @Query("SELECT * FROM allergen ORDER BY id ASC")
    fun getAllAllergens(): Flow<List<Allergen>>

    @Query("SELECT * FROM restaurant ORDER BY id ASC")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT * FROM country ORDER BY id ASC")
    fun getAllCountries(): Flow<List<Country>>

    @Query("SELECT * FROM dish ORDER BY id ASC")
    fun getAllDishes(): Flow<List<Dish>>

    @Query("SELECT * FROM dish WHERE name LIKE :pattern ORDER BY id ASC")
    fun getDishesContaining(pattern: String): Flow<List<Dish>>

}