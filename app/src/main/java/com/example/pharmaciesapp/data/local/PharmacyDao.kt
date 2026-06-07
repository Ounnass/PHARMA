package com.example.pharmaciesapp.data.local

import androidx.room.*
import com.example.pharmaciesapp.data.model.Pharmacy
import kotlinx.coroutines.flow.Flow

@Dao
interface PharmacyDao {
    @Query("SELECT * FROM pharmacies")
    fun getAllPharmacies(): Flow<List<Pharmacy>>

    @Query("SELECT * FROM pharmacies WHERE id = :id")
    suspend fun getPharmacyById(id: String): Pharmacy?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pharmacies: List<Pharmacy>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPharmacy(pharmacy: Pharmacy)

    @Update
    suspend fun updatePharmacy(pharmacy: Pharmacy)

    @Delete
    suspend fun deletePharmacy(pharmacy: Pharmacy)

    @Query("DELETE FROM pharmacies")
    suspend fun clearAll()
}