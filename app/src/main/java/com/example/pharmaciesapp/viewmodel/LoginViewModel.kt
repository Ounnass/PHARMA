package com.example.pharmaciesapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.pharmaciesapp.data.local.SessionManager

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(sessionManager.isLoggedIn())

    fun onLoginClick(onSuccess: () -> Unit) {
        if (email == "admin@gmail.com" && password == "12345") {
            sessionManager.saveAuthToken("fake-jwt-token")
            isLoggedIn = true
            onSuccess()
        } else {
            error = "Identifiants invalides"
        }
    }
}