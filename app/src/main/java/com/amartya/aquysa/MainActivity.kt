package com.amartya.aquysa

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amartya.aquysa.ui.screens.MainApp
import com.amartya.aquysa.ui.theme.AquysaTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val snackbarMessage = mutableStateOf<String?>(null)

    private val splashViewModel : SplashViewmodel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                snackbarMessage.value = "Notifications disabled. Reminders won't work."
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            AquysaTheme {
                val viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = MainViewModelFactory(application)
                )
                val message by snackbarMessage
                MainApp(
                    viewModel = viewModel,
                    snackbarMessage = message,
                    onSnackbarDismiss = { snackbarMessage.value = null }
                )
                installSplashScreen().apply {
                    setKeepOnScreenCondition{
                        splashViewModel.isSplashShow.value
                    }
                }
            }
        }
    }

}
@Composable
fun AppBottomNavigation(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, "Analysis") },
            label = { Text("Analysis") },
            selected = currentRoute == "analysis",
            onClick = { navController.navigate("analysis") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") { launchSingleTop = true } }
        )
    }
}
