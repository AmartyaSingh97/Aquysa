package com.amartya.aquysa.widget

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.amartya.aquysa.data.AppDatabase
import com.amartya.aquysa.data.WaterRepository
import kotlinx.coroutines.flow.first

val ADD_WATER_ACTION = ActionParameters.Key<Int>("add_water_amount")

val intakeKey = intPreferencesKey("current_intake")
val goalKey = intPreferencesKey("daily_goal")

class AddWaterActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val amount = parameters[ADD_WATER_ACTION] ?: 250
        val repository = WaterRepository(AppDatabase.getDatabase(context).waterDao())

        repository.addWater(amount)

        val newIntake = repository.getTodayIntake().first()
        val sharedPrefs = context.getSharedPreferences("AquaTrackPrefs", Context.MODE_PRIVATE)
        val dailyGoal = sharedPrefs.getInt("daily_goal", 4000)

        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[intakeKey] = newIntake
            prefs[goalKey] = dailyGoal
        }

        WaterWidget().update(context, glanceId)
    }
}
