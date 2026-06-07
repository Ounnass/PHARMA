package com.example.pharmaciesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pharmaciesapp.data.local.SessionManager
import com.example.pharmaciesapp.repository.PharmacyRepository

class ViewModelFactory(
    private val sessionManager: SessionManager? = null,
    private val repository: PharmacyRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(sessionManager!!) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository!!) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository!!) as T
        }
        if (modelClass.isAssignableFrom(AddEditViewModel::class.java)) {
            return AddEditViewModel(repository!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}