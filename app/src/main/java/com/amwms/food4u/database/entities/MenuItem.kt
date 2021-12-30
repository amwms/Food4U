package com.amwms.food4u.database.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MenuItem(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "dish_id") val dishId: Int,
    @NonNull @ColumnInfo(name = "country_id") val countryId: Int,
    @NonNull @ColumnInfo(name = "restaurant_id") val restaurantId: Int
)