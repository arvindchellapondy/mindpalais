package com.mindpalais.android

import DatabaseHelper
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class MindpalaisApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            AndroidThreeTen.init(this)

            // Create the database table here...
            val dbHelper = DatabaseHelper(applicationContext)
            val db = dbHelper.writableDatabase

        }
    }