package com.amartya.aquysa.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date

class WaterRepository(private val waterDao: WaterDao) {
    val allIntakes: Flow<List<WaterIntake>> = waterDao.getAllIntakes()


    fun getTodayIntake(): Flow<Int> {
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        return waterDao.getTodayIntake(startOfDay).map { it ?: 0 }
    }


    suspend fun addWater(amount: Int) {
        waterDao.insert(WaterIntake(amount = amount, timestamp = Date()))
    }
}
