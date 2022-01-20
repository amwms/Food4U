package com.amwms.food4u.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity (primaryKeys = ["set_id", "dish_id"])
data class DishSet(
    @NonNull @ColumnInfo(name = "set_id") val setId: Int,
    @NonNull @ColumnInfo(name = "dish_id") val dishId: Int
)