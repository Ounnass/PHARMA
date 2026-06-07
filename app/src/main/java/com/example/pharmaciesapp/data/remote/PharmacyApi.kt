package com.example.pharmaciesapp.data.remote

import com.example.pharmaciesapp.data.model.Pharmacy
import retrofit2.http.*

interface PharmacyApi {
    @GET("pharma")
    suspend fun getPharmacies(): List<Pharmacy>

    @GET("pharma/{id}")
    suspend fun getPharmacyById(@Path("id") id: String): Pharmacy

    @POST("pharma")
    suspend fun addPharmacy(@Body pharmacy: Pharmacy): Pharmacy

    @PUT("pharma/{id}")
    suspend fun updatePharmacy(@Path("id") id: String, @Body pharmacy: Pharmacy): Pharmacy

    @DELETE("pharma/{id}")
    suspend fun deletePharmacy(@Path("id") id: String)

    companion object {
        const val BASE_URL = "https://69e0cb7729c070e6597c0a93.mockapi.io/api/PharmaciesApp/"
    }
}