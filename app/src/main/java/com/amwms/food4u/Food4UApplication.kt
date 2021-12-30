package com.amwms.food4u

import android.app.Application
import com.amwms.food4u.database.AppDatabase

class Food4UApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
