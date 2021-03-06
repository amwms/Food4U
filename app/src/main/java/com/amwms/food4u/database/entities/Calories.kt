package com.amwms.food4u.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calories(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "menuitem_id") val menuItemId: Int,
    @NonNull @ColumnInfo(name = "energy_kj") val energy_kj: Int,
    @NonNull @ColumnInfo(name = "energy_kcal") val energy_kcal: Int,
    @NonNull @ColumnInfo(name = "fat_g") val fat_g: Double,
    @NonNull @ColumnInfo(name = "saturated_fat_g") val saturatedFat_g: Double,
    @NonNull @ColumnInfo(name = "carbohydrates_g") val carbohydrates_g: Double,
    @NonNull @ColumnInfo(name = "sugar_g") val sugar_g: Double,
    @NonNull @ColumnInfo(name = "fiber_g") val fiber_g: Double,
    @NonNull @ColumnInfo(name = "protein_g") val protein_g: Double,
    @NonNull @ColumnInfo(name = "salt_g") val salt_g: Double,
)