package com.amartya.aquysa

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.amartya.aquysa.data.AppDatabase
import com.amartya.aquysa.data.WaterIntake
import com.amartya.aquysa.data.WaterRepository
import com.amartya.aquysa.widget.ReminderWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WaterRepository
    val todayIntake: StateFlow<Int>
    val allIntakes: StateFlow<List<WaterIntake>>
    private val sharedPrefs = application.getSharedPreferences("AquaTrackPrefs", Context.MODE_PRIVATE)
    private val _isOnboardingNeeded = mutableStateOf(true)
    val isOnboardingNeeded: MutableState<Boolean> = _isOnboardingNeeded
    private val _dailyGoal = mutableIntStateOf(4000)
    val dailyGoal: MutableState<Int> = _dailyGoal

    init {
        val waterDao = AppDatabase.getDatabase(application).waterDao()
        repository = WaterRepository(waterDao)

        todayIntake = repository.getTodayIntake()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = 0
            )

        allIntakes = repository.allIntakes
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

        checkIfFirstLaunch()
    }

    private fun checkIfFirstLaunch() {
        val isFirstLaunch = sharedPrefs.getBoolean("is_first_launch", true)
        _isOnboardingNeeded.value = isFirstLaunch
        if (!isFirstLaunch) {
            _dailyGoal.intValue = sharedPrefs.getInt("daily_goal", 4000)
        }
    }

    fun addWater(amount: Int) {
        viewModelScope.launch {
            repository.addWater(amount)
        }
    }

    fun completeOnboarding(goal: Int, reminderIntervalHours: Int) {
        _dailyGoal.intValue = goal
        with(sharedPrefs.edit()) {
            putInt("daily_goal", goal)
            putInt("reminder_interval", reminderIntervalHours)
            putBoolean("is_first_launch", false)
            apply()
        }
        scheduleReminders(getApplication(), reminderIntervalHours)

        val intent = Intent("com.amartya.aquysa.ACTION_UPDATE_WIDGET").setClassName(
            getApplication<Application>().packageName,
            "com.amartya.aquysa.widget.WaterWidgetReceiver"
        )
        getApplication<Application>().sendBroadcast(intent)

        _isOnboardingNeeded.value = false
    }


    fun setDailyGoal(goal: Int) {
        _dailyGoal.intValue = goal
        with(sharedPrefs.edit()) {
            putInt("daily_goal", goal)
            apply()
        }

        val intent = Intent("com.amartya.aquysa.ACTION_UPDATE_WIDGET").setClassName(
            getApplication<Application>().packageName,
            "com.amartya.aquysa.widget.WaterWidgetReceiver"
        )
        getApplication<Application>().sendBroadcast(intent)
        Toast.makeText(getApplication(), "Daily goal set to $goal ml", Toast.LENGTH_SHORT).show()
    }


    fun setReminderInterval(intervalHours: Int) {
        with(sharedPrefs.edit()) {
            putInt("reminder_interval", intervalHours)
            apply()
        }
        scheduleReminders(getApplication(), intervalHours)
    }

    fun scheduleReminders(context: Context, intervalHours: Int) {
        if (intervalHours < 1) return


        val workManager = WorkManager.getInstance(context)
        val repeatInterval = intervalHours.toLong()


        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(repeatInterval, TimeUnit.HOURS)
            .build()


        workManager.enqueueUniquePeriodicWork(
            "water_reminder_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderRequest
        )
    }

}


class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
