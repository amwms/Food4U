package com.amwms.food4u.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amwms.food4u.database.entities.*


@Database(
    entities = arrayOf(Allergen::class,
        Calories::class,
        Country::class,
        Dish::class,
        MenuItem::class,
        MenuItemAllergens::class,
        Restaurant::class),
    version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun Food4UDao(): Food4UDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/food4u_database.db")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
