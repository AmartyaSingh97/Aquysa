package com.amartya.aquysa.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.amartya.aquysa.R
import com.amartya.aquysa.widget.ADD_WATER_ACTION
import com.amartya.aquysa.widget.AddWaterActionCallback
import com.amartya.aquysa.widget.goalKey
import com.amartya.aquysa.widget.intakeKey

@Composable
fun WidgetContent() {
    val prefs = currentState<Preferences>()
    val currentIntake = prefs[intakeKey] ?: 0
    val dailyGoal = prefs[goalKey] ?: 4000
    val progress = (currentIntake.toFloat() / (dailyGoal.toFloat()).coerceAtLeast(1f)).coerceIn(0f, 1f)


    Row(
        modifier = GlanceModifier
            .size(width = 350.dp, height = 100.dp)
            .background(Color(0xFFF0F8FF))
            .cornerRadius(128.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Image(
            provider = ImageProvider(R.drawable.logo),
            contentDescription = "Mascot",
            modifier = GlanceModifier.size(60.dp)
        )


        Spacer(GlanceModifier.width(12.dp))

        Column(
            modifier = GlanceModifier.defaultWeight(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            Text(
                text = "$currentIntake ml / $dailyGoal ml",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF4CA5AE)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(GlanceModifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = GlanceModifier.fillMaxWidth().height(10.dp).cornerRadius(10.dp),
                color = ColorProvider(Color(0xFF00BFFF)),
                backgroundColor = ColorProvider(Color(0xFFB0E0E6)),
            )
        }


        Spacer(GlanceModifier.width(12.dp))

        Image(
            provider = ImageProvider(R.drawable.plus),
            contentDescription = "Add 250ml",
            modifier = GlanceModifier.size(36.dp).clickable(
                onClick = actionRunCallback<AddWaterActionCallback>(
                    actionParametersOf(ADD_WATER_ACTION to 250)
                )
            )
        )
    }
}
