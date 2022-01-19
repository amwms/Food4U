package com.amwms.food4u.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavoriteSet(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "user_id") val userId: String,
    @NonNull @ColumnInfo(name = "set_name") val setName: String,
    @NonNull @ColumnInfo(name = "calorie_count") val calorieCount: Int
)