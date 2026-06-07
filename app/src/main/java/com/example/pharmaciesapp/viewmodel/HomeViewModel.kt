package com.example.pharmaciesapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaciesapp.data.model.Pharmacy
import com.example.pharmaciesapp.repository.PharmacyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: PharmacyRepository) : ViewModel() {

    val pharmacies = repository.pharmacies.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    var isLoading by mutableStateOf(false)
        private set
    var isOffline by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.refreshPharmacies()
            isOffline = result.isFailure
            if (result.isFailure) {
                errorMessage = "Erreur de connexion"
            }
            isLoading = false
        }
    }
}