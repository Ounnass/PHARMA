package com.example.pharmaciesapp.repository

import com.example.pharmaciesapp.data.local.PharmacyDao
import com.example.pharmaciesapp.data.model.Pharmacy
import com.example.pharmaciesapp.data.remote.PharmacyApi
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class PharmacyRepository(
    private val api: PharmacyApi,
    private val dao: PharmacyDao
) {
    val pharmacies: Flow<List<Pharmacy>> = dao.getAllPharmacies()

    suspend fun refreshPharmacies(): Result<Unit> {
        return try {
            val remotePharmacies = api.getPharmacies()
            dao.clearAll()
            dao.insertAll(remotePharmacies)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPharmacyById(id: String): Pharmacy? {
        return try {
            val remote = api.getPharmacyById(id)
            dao.insertPharmacy(remote)
            remote
        } catch (e: Exception) {
            dao.getPharmacyById(id)
        }
    }

    suspend fun addPharmacy(pharmacy: Pharmacy): Result<Pharmacy> {
        return try {
            val created = api.addPharmacy(pharmacy)
            dao.insertPharmacy(created)
            Result.success(created)
        } catch (e: Exception) {
            // In a real app we might queue this for later sync
            Result.failure(e)
        }
    }

    suspend fun updatePharmacy(id: String, pharmacy: Pharmacy): Result<Pharmacy> {
        return try {
            val updated = api.updatePharmacy(id, pharmacy)
            dao.insertPharmacy(updated)
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePharmacy(id: String): Result<Unit> {
        return try {
            api.deletePharmacy(id)
            val pharmacy = dao.getPharmacyById(id)
            if (pharmacy != null) {
                dao.deletePharmacy(pharmacy)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}