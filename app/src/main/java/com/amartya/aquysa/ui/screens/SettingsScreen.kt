package com.amartya.aquysa.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amartya.aquysa.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    var goalText by remember { mutableStateOf(viewModel.dailyGoal.value.toString()) }
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("AquaTrackPrefs", Context.MODE_PRIVATE) }
    var reminderInterval by remember { mutableFloatStateOf(sharedPrefs.getInt("reminder_interval", 2).toFloat()) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(64.dp))


        OutlinedTextField(
            value = goalText,
            onValueChange = { goalText = it },
            label = { Text("Daily Goal (ml)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Button(onClick = {
            if(goalText.isNotEmpty()){
                focusManager.clearFocus()
                keyboardController?.hide()
                viewModel.setDailyGoal(goalText.toIntOrNull() ?: 4000)
            }
        }) {
            Text("Save Goal")
        }

        Spacer(Modifier.height(32.dp))

        Text("Reminders", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))


        Text("Remind me every: ${reminderInterval.toInt()} hours", style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = reminderInterval,
            onValueChange = { reminderInterval = it },
            valueRange = 1f..8f,
            steps = 6,
            onValueChangeFinished = {
                viewModel.setReminderInterval(reminderInterval.toInt())
            }
        )
    }
}
