package com.example.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.viewmodel.MainViewModelFactory
import com.example.testapp.network.Api
import com.example.testapp.network.RetrofitInstance
import com.example.testapp.repo.MainRepo
import com.example.testapp.ui.screens.CreateAccountScreen
import com.example.testapp.ui.screens.LoginScreen
import com.example.testapp.ui.screens.RegisterProfileScreen
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.utils.Preference
import com.example.testapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    lateinit var retrofitInstance: RetrofitInstance
    lateinit var api: Api
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = Preference.getInstance(this)
        retrofitInstance = RetrofitInstance(prefs.getStringValue("token"))
        api = retrofitInstance.api
        val repository = MainRepo(api)

        val viewModelProviderFactory = MainViewModelFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        enableEdgeToEdge()
        setContent {
            TestAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login_screen"
                ) {
                    composable("login_screen") {
                        LoginScreen(navController, viewModel, prefs)
                    }
                    composable("create_account") {
                        CreateAccountScreen(navController, viewModel, prefs)
                    }
                    composable("register_profile") {
                        RegisterProfileScreen(navController, viewModel, prefs)
                    }
                }
            }
        }
    }

    fun updateRetrofitInstance() {
        retrofitInstance.updateToken("new_token")
        api = retrofitInstance.api
    }
}