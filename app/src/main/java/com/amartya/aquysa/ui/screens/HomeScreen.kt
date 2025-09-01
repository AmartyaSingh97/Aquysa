package com.amartya.aquysa.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amartya.aquysa.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val todayIntake by viewModel.todayIntake.collectAsState()
    val dailyGoal by viewModel.dailyGoal


    val progress = (todayIntake.toFloat() / dailyGoal.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progressAnimation")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text("Aquysa", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)


        Box(contentAlignment = Alignment.Center) {
            CircularProgress(
                progress = animatedProgress,
                size = 250.dp
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$todayIntake",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "/ $dailyGoal ml",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AddWaterButton(amount = 250, onAdd = { viewModel.addWater(it) })
            AddWaterButton(amount = 500, onAdd = { viewModel.addWater(it) })
            AddWaterButton(amount = 750, onAdd = { viewModel.addWater(it) })
        }
    }
}

@Composable
fun CircularProgress(
    progress: Float,
    size: Dp,
    strokeWidth: Dp = 16.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = Modifier.size(size)) {
        drawArc(
            color = backgroundColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360 * progress,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
            size = Size(size.toPx(), size.toPx())
        )
    }
}

