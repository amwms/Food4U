package com.amwms.food4u.database

import androidx.room.Dao
import androidx.room.Query
import com.amwms.food4u.database.entities.*
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

    @Query("SELECT * FROM dish " +
            "WHERE name LIKE :pattern " +
            "AND id IN (SELECT dish_id FROM menuitem WHERE country_id = :countryId AND restaurant_id = :restaurantId) " +
            "ORDER BY id ASC")
    fun getDishesContaining(pattern: String, countryId: Int, restaurantId: Int): Flow<List<Dish>>

    @Query("SELECT * FROM calories WHERE menuitem_id IN (SELECT id FROM menuitem WHERE :dishId = dish_id)")
    fun getDishIdCalories(dishId: Int): Flow<List<Calories>>

    @Query("SELECT name FROM dish WHERE id = :dishId")
    fun getDishNameFromId(dishId: Int): Flow<String>

    @Query("SELECT * FROM dish WHERE id IN (SELECT dish_id FROM menuitem WHERE (country_id = :countryId AND restaurant_id = :restaurantId) AND id IN (SELECT menuitem_id FROM calories WHERE energy_kcal <= :maxCalories AND energy_kcal >= :minCalories)) ORDER BY id ASC")
    fun getDishesWithConstraints(maxCalories: Int, minCalories: Int,
                                 countryId: Int, restaurantId: Int): Flow<List<Dish>>
}