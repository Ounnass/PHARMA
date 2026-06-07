package com.example.pharmaciesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pharmaciesapp.data.model.Pharmacy

@Database(entities = [Pharmacy::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val pharmacyDao: PharmacyDao
}