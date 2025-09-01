package com.amartya.aquysa.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.amartya.aquysa.data.AppDatabase
import com.amartya.aquysa.data.WaterRepository
import com.amartya.aquysa.ui.screens.WidgetContent
import kotlinx.coroutines.flow.first

class WaterWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val repository = WaterRepository(AppDatabase.getDatabase(context).waterDao())
        val currentIntake = repository.getTodayIntake().first()
        val sharedPrefs = context.getSharedPreferences("AquaTrackPrefs", Context.MODE_PRIVATE)
        val dailyGoal = sharedPrefs.getInt("daily_goal", 4000)

        updateAppWidgetState(context, id) { prefs ->
            prefs[intakeKey] = currentIntake
            prefs[goalKey] = dailyGoal
        }

        provideContent {
            WidgetContent()
        }
    }
}