package com.amwms.food4u.database.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (primaryKeys = ["menuitem_id", "allergen_id"])
data class MenuItemAllergens(
    @NonNull @ColumnInfo(name = "menuitem_id") val menuItemId: Int,
    @NonNull @ColumnInfo(name = "allergen_id") val allergenId: Int
)