package com.example.pharmaciesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.pharmaciesapp.data.local.AppDatabase
import com.example.pharmaciesapp.data.local.SessionManager
import com.example.pharmaciesapp.data.remote.PharmacyApi
import com.example.pharmaciesapp.repository.PharmacyRepository
import com.example.pharmaciesapp.ui.addedit.AddEditPharmacyScreen
import com.example.pharmaciesapp.ui.detail.DetailScreen
import com.example.pharmaciesapp.ui.home.HomeScreen
import com.example.pharmaciesapp.ui.login.LoginScreen
import com.example.pharmaciesapp.ui.theme.PharmaciesTheme
import com.example.pharmaciesapp.viewmodel.AddEditViewModel
import com.example.pharmaciesapp.viewmodel.DetailViewModel
import com.example.pharmaciesapp.viewmodel.HomeViewModel
import com.example.pharmaciesapp.viewmodel.LoginViewModel
import com.example.pharmaciesapp.viewmodel.ViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "pharmacy-db"
        ).build()

        val api = Retrofit.Builder()
            .baseUrl(PharmacyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PharmacyApi::class.java)

        val repository = PharmacyRepository(api, db.pharmacyDao)

        setContent {
            PharmaciesTheme {
                PharmacyApp(sessionManager, repository)
            }
        }
    }
}

@Composable
fun PharmacyApp(sessionManager: SessionManager, repository: PharmacyRepository) {
    val navController = rememberNavController()
    val startDestination = if (sessionManager.isLoggedIn()) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            val viewModel: LoginViewModel = viewModel(factory = ViewModelFactory(sessionManager = sessionManager))
            LoginScreen(viewModel) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        
        composable("home") {
            val viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(repository = repository))
            HomeScreen(
                viewModel = viewModel,
                onPharmacyClick = { id -> navController.navigate("detail/$id") },
                onAddPharmacyClick = { navController.navigate("add") },
                onLogout = {
                    sessionManager.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "detail/{pharmacyId}",
            arguments = listOf(navArgument("pharmacyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("pharmacyId") ?: ""
            val viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(repository = repository))
            DetailScreen(
                id = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onEdit = { pharmacyId -> navController.navigate("edit/$pharmacyId") },
                onDeleted = { navController.popBackStack() }
            )
        }

        composable("add") {
            val viewModel: AddEditViewModel = viewModel(factory = ViewModelFactory(repository = repository))
            AddEditPharmacyScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = "edit/{pharmacyId}",
            arguments = listOf(navArgument("pharmacyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("pharmacyId")
            val viewModel: AddEditViewModel = viewModel(factory = ViewModelFactory(repository = repository))
            AddEditPharmacyScreen(
                id = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
    }
}