package com.amwms.food4u.database.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (primaryKeys = ["menuitem_id", "allergen_id"])
data class MenuItemAllergens(
    @NonNull @ColumnInfo(name = "menuitem_id") val menuitem_id: Int,
    @NonNull @ColumnInfo(name = "allergen_id") val allergen_id: Int
)