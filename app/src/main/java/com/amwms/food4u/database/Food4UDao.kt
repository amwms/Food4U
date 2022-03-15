package com.amwms.food4u.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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

    @Query("SELECT * FROM dish " +
            "WHERE id IN (SELECT dish_id FROM menuitem WHERE (country_id = :countryId AND restaurant_id = :restaurantId) " +
            "AND id IN (SELECT menuitem_id FROM calories WHERE energy_kcal <= :maxCalories AND energy_kcal >= :minCalories) " +
            "AND id NOT IN (SELECT menuitem_id FROM MenuItemAllergens WHERE allergen_id IN (SELECT id FROM allergen WHERE name IN (:allergenNames)) ) ) " +
            "ORDER BY id ASC")
    fun getDishesWithConstraints(allergenNames: List<String>, maxCalories: Int, minCalories: Int,
                                 countryId: Int, restaurantId: Int): Flow<List<Dish>>

    @Query("SELECT COUNT(*) FROM allergen")
    fun getNumberOfAllergens(): Int

    @Query("SELECT name FROM allergen")
    fun getAllAllergensNames(): List<String>

    @Query("SELECT * FROM dish " +
            "WHERE id IN (SELECT dish_id FROM menuitem WHERE (country_id = :countryId AND restaurant_id = :restaurantId) " +
            "AND id IN (SELECT menuitem_id FROM calories WHERE energy_kcal <= :maxCalories AND energy_kcal >= :minCalories) " +
            "AND id NOT IN (SELECT menuitem_id FROM MenuItemAllergens WHERE allergen_id IN (SELECT id FROM allergen WHERE name IN (:allergenNames)) ) ) " +
            "ORDER BY id ASC")
    fun getDishesWithConstraintsList(allergenNames: List<String>, maxCalories: Int, minCalories: Int,
                                 countryId: Int, restaurantId: Int): List<Dish>

    @Query("SELECT c.energy_kcal FROM (SELECT * FROM dish WHERE id = :dishId) as d LEFT JOIN menuitem i ON d.id = i.dish_id LEFT JOIN calories c ON i.id = c.menuitem_id GROUP BY d.id")
    fun getDishIdEnergy(dishId: Int): Int

    @Insert(entity = DishSet::class)
    fun insertDishIntoSet(dishSet: DishSet)

    @Insert(entity = FavoriteSet::class)
    fun insertNewSet(set: FavoriteSet)

    @Delete(entity = FavoriteSet::class)
    fun deleteSet(set: FavoriteSet)

    @Delete(entity = DishSet::class)
    fun deleteDishInSet(dishSet: DishSet)

    @Query("SELECT * FROM dishset WHERE dish_id = :dishId AND set_id = :setId")
    fun getDishSetById(dishId: Int, setId: Int): DishSet

    @Query("SELECT * FROM favoriteset WHERE id = :setId")
    fun getFavoriteSetById(setId: Int): FavoriteSet

    @Query("SELECT COUNT(id) FROM favoriteset")
    fun getSetCount(): Int

    @Query("SELECT MAX(id) FROM favoriteset")
    fun getHighestSetId(): Int

    @Query("SELECT * FROM favoriteset WHERE user_id = :userId")
    fun getAllFavoriteSets(userId: String): Flow<List<FavoriteSet>>

    @Query("SELECT * FROM dish WHERE id IN (SELECT dish_id FROM dishset WHERE set_id = :setId GROUP BY dish_id)")
    fun getAllDishesInSet(setId: Int): Flow<List<Dish>>

    @Query("SELECT * FROM dish WHERE id IN (SELECT dish_id FROM dishset WHERE set_id = :setId GROUP BY dish_id)")
    fun getAllDishesInSetList(setId: Int): List<Dish>

    @Query("SELECT SUM(c.energy_kcal) FROM " +
            "(SELECT * FROM dish WHERE id " +
            "IN (SELECT dish_id FROM dishset WHERE set_id = :setId GROUP BY dish_id)) as d " +
            "LEFT JOIN menuitem i ON d.id = i.dish_id " +
            "LEFT JOIN calories c ON i.id = c.menuitem_id GROUP BY d.id")
    fun getCalorieSumInSet(setId: Int): Int

    @Query("SELECT COUNT(*) FROM dish WHERE id = :dishId")
    fun getDishIdExists(dishId: Int): Int
}