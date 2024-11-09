package com.josval.firetasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josval.firetasks.view.pages.HomePage
import com.josval.firetasks.view.pages.LoginPage
import com.josval.firetasks.view.pages.ProfilePage
import com.josval.firetasks.view.pages.SignupPage
import com.josval.firetasks.viewmodel.AuthState
import com.josval.firetasks.viewmodel.AuthViewModel
import com.josval.firetasks.viewmodel.TaskViewModel

@Composable
fun FiretasksNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.popBackStack()
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
            is AuthState.Unauthenticated -> {
                navController.popBackStack()
                navController.navigate("login") {
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel, taskViewModel)
        }
        composable("profile") {
            ProfilePage(modifier, navController, authViewModel)
        }
    })
}