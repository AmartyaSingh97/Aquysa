package com.amartya.aquysa.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amartya.aquysa.MainViewModel

@Composable
fun OnboardingScreen(viewModel: MainViewModel) {
    var goalText by remember { mutableStateOf("4000") }
    var reminderInterval by remember { mutableFloatStateOf(1f) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to Aquysa!", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(16.dp))
        Text("Let's set up your daily hydration goals.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(48.dp))


        OutlinedTextField(
            value = goalText,
            onValueChange = { goalText = it },
            label = { Text("Daily Goal (ml)", color = MaterialTheme.colorScheme.onSurface) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(32.dp))


        Text("Remind me every: ${reminderInterval.toInt()} hours", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Slider(
            value = reminderInterval,
            onValueChange = { reminderInterval = it },
            valueRange = 1f..8f,
            steps = 6
        )
        Spacer(Modifier.height(48.dp))


        Button(
            onClick = {
                val goal = goalText.toIntOrNull() ?: 4000
                viewModel.completeOnboarding(goal, reminderInterval.toInt())
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Get Started")
        }
    }
}
