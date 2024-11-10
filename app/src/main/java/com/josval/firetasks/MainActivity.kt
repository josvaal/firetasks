package com.josval.firetasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.josval.firetasks.ui.theme.FireTasksTheme
import com.josval.firetasks.viewmodel.AuthViewModel
import com.josval.firetasks.viewmodel.FirestoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authViewModel: AuthViewModel by viewModels()
        val firestoreViewModel: FirestoreViewModel by viewModels()

        setContent {
            FireTasksTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = isSystemInDarkTheme()
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )

                    FiretasksNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel,
                        firestoreViewModel = firestoreViewModel
                    )
                }
            }
        }
    }
}
