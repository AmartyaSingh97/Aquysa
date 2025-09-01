package com.amartya.aquysa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface WaterDao {

    @Insert
    suspend fun insert(waterIntake: WaterIntake)

    @Query("SELECT * FROM water_intake ORDER BY timestamp DESC")
    fun getAllIntakes(): Flow<List<WaterIntake>>


    @Query("SELECT SUM(amount) FROM water_intake WHERE timestamp >= :startOfDay")
    fun getTodayIntake(startOfDay: Date): Flow<Int?>
}
