package com.example.pharmaciesapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaciesapp.data.model.Pharmacy
import com.example.pharmaciesapp.repository.PharmacyRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AddEditViewModel(private val repository: PharmacyRepository) : ViewModel() {
    var name by mutableStateOf("")
    var address by mutableStateOf("")
    var phone by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadPharmacy(id: String) {
        viewModelScope.launch {
            isLoading = true
            val pharmacy = repository.getPharmacyById(id)
            if (pharmacy != null) {
                name = pharmacy.name
                address = pharmacy.address
                phone = pharmacy.phone
            }
            isLoading = false
        }
    }

    fun savePharmacy(id: String?, onSuccess: () -> Unit) {
        if (name.isBlank() || address.isBlank() || phone.isBlank()) {
            errorMessage = "Veuillez remplir tous les champs"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val pharmacy = Pharmacy(
                id = id ?: UUID.randomUUID().toString(),
                name = name,
                address = address,
                phone = phone
            )

            val result = if (id == null) {
                repository.addPharmacy(pharmacy)
            } else {
                repository.updatePharmacy(id, pharmacy)
            }

            if (result.isSuccess) {
                onSuccess()
            } else {
                errorMessage = "Erreur lors de l'enregistrement"
            }
            isLoading = false
        }
    }
}