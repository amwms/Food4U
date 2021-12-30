package com.amwms.food4u.database.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MenuItem(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "dish_id") val dish_id: Int,
    @NonNull @ColumnInfo(name = "country_id") val country_id: Int,
    @NonNull @ColumnInfo(name = "restaurant_id") val restaurant_id: Int
)