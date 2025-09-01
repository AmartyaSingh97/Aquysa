package com.amartya.aquysa.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amartya.aquysa.AppBottomNavigation
import com.amartya.aquysa.MainViewModel

@Composable
fun MainApp(
    viewModel: MainViewModel,
    snackbarMessage: String?,
    onSnackbarDismiss: () -> Unit
) {
    val navController = rememberNavController()
    val isOnboardingNeeded by viewModel.isOnboardingNeeded
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage != null) {
            snackbarHostState.showSnackbar(
                message = snackbarMessage,
                duration = SnackbarDuration.Short
            )
            onSnackbarDismiss()
        }
    }


    Crossfade(targetState = isOnboardingNeeded, label = "OnboardingCrossfade") { needsOnboarding ->
        if (needsOnboarding) {
            OnboardingScreen(viewModel = viewModel)
        } else {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = { AppBottomNavigation(navController) }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("home") { HomeScreen(viewModel) }
                    composable("analysis") { AnalysisScreen(viewModel) }
                    composable("settings") { SettingsScreen(viewModel) }
                }
            }
        }
    }
}

