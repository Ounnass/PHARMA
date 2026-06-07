package com.example.pharmaciesapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaciesapp.data.model.Pharmacy
import com.example.pharmaciesapp.repository.PharmacyRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: PharmacyRepository) : ViewModel() {
    var pharmacy by mutableStateOf<Pharmacy?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun loadPharmacy(id: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = repository.getPharmacyById(id)
            if (result != null) {
                pharmacy = result
            } else {
                error = "Impossible de charger les détails"
            }
            isLoading = false
        }
    }

    fun deletePharmacy(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = repository.deletePharmacy(id)
            if (result.isSuccess) {
                onSuccess()
            } else {
                error = "Erreur lors de la suppression"
            }
        }
    }
}