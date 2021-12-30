package com.amwms.food4u.database.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Allergen(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "name") val name: String
)