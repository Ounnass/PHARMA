package com.example.pharmaciesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pharmacies")
data class Pharmacy(
    @PrimaryKey
    val id: String,
    val name: String,
    val address: String,
    val phone: String
)