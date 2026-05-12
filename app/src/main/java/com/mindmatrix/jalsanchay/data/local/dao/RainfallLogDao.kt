package com.mindmatrix.jalsanchay.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindmatrix.jalsanchay.data.local.entity.RainfallLogEntity
import com.mindmatrix.jalsanchay.data.local.model.AllTimeStatsData
import com.mindmatrix.jalsanchay.data.local.model.DailyAggregateData
import com.mindmatrix.jalsanchay.data.local.model.MonthlyStats
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface RainfallLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: RainfallLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiple(entries: List<RainfallLogEntity>)

    @Delete
    suspend fun delete(entry: RainfallLogEntity)

    @Query("DELETE FROM rainfall_log WHERE user_id = :userId")
    suspend fun deleteByUserId(userId: String)

    @Query("SELECT * FROM rainfall_log WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): RainfallLogEntity?

    @Query("SELECT * FROM rainfall_log WHERE user_id = :userId AND date = :date ORDER BY timestamp DESC")
    suspend fun getByDate(userId: String, date: LocalDate): List<RainfallLogEntity>

    @Query("SELECT * FROM rainfall_log WHERE user_id = :userId AND date = :date ORDER BY timestamp DESC")
    fun observeTodayEntries(userId: String, date: LocalDate): Flow<List<RainfallLogEntity>>

    @Query("SELECT * FROM rainfall_log WHERE user_id = :userId AND date = :date ORDER BY timestamp DESC")
    suspend fun getTodayEntries(userId: String, date: LocalDate): List<RainfallLogEntity>

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log WHERE user_id = :userId AND date = :date")
    suspend fun getTodayTotalVolume(userId: String, date: LocalDate): Double

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log WHERE user_id = :userId AND date = :date")
    fun observeTodayTotalVolume(userId: String, date: LocalDate): Flow<Double>

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log WHERE user_id = :userId")
    fun observeAllTimeTotalVolume(userId: String): Flow<Double>

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log")
    fun observeGlobalTotalVolume(): Flow<Double>

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log WHERE date >= :startDate AND date <= :endDate")
    fun observeGlobalMonthlyTotalVolume(startDate: LocalDate, endDate: LocalDate): Flow<Double>

    @Query("SELECT COUNT(*) FROM rainfall_log")
    fun observeGlobalEntryCount(): Flow<Int>

    @Query("SELECT * FROM rainfall_log WHERE user_id = :userId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    suspend fun getMonthlyEntries(userId: String, startDate: LocalDate, endDate: LocalDate): List<RainfallLogEntity>

    @Query("SELECT * FROM rainfall_log WHERE user_id = :userId AND date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun observeMonthlyEntries(userId: String, startDate: LocalDate, endDate: LocalDate): Flow<List<RainfallLogEntity>>

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log WHERE user_id = :userId AND date >= :startDate AND date <= :endDate")
    suspend fun getMonthlyTotalVolume(userId: String, startDate: LocalDate, endDate: LocalDate): Double

    @Query("SELECT COALESCE(SUM(volume_calculated), 0.0) FROM rainfall_log WHERE user_id = :userId AND date >= :startDate AND date <= :endDate")
    fun observeMonthlyTotalVolume(userId: String, startDate: LocalDate, endDate: LocalDate): Flow<Double>

    @Query(
        """
        SELECT COUNT(*) AS entry_count,
               COUNT(DISTINCT date) AS rain_days,
               COALESCE(AVG(rainfall_mm), 0.0) AS avg_rainfall,
               COALESCE(MAX(rainfall_mm), 0.0) AS max_rainfall,
               COALESCE(MIN(rainfall_mm), 0.0) AS min_rainfall,
               COALESCE(SUM(volume_calculated), 0.0) AS total_volume
        FROM rainfall_log
        WHERE user_id = :userId AND date >= :startDate AND date <= :endDate
        """
    )
    suspend fun getMonthlyStats(userId: String, startDate: LocalDate, endDate: LocalDate): MonthlyStats?

    @Query(
        """
        SELECT date,
               COALESCE(SUM(volume_calculated), 0.0) AS daily_volume,
               COUNT(*) AS entry_count
        FROM rainfall_log
        WHERE user_id = :userId AND date >= :startDate
        GROUP BY date
        ORDER BY date ASC
        """
    )
    suspend fun getLast7DaysData(userId: String, startDate: LocalDate): List<DailyAggregateData>

    @Query(
        """
        SELECT date,
               COALESCE(SUM(volume_calculated), 0.0) AS daily_volume,
               COUNT(*) AS entry_count
        FROM rainfall_log
        WHERE user_id = :userId AND date >= :startDate
        GROUP BY date
        ORDER BY date ASC
        """
    )
    fun observeLast7DaysData(userId: String, startDate: LocalDate): Flow<List<DailyAggregateData>>

    @Query(
        """
        SELECT COUNT(*) AS total_entries,
               COUNT(DISTINCT date) AS rain_days,
               COALESCE(SUM(volume_calculated), 0.0) AS total_volume,
               COALESCE(MAX(rainfall_mm), 0.0) AS max_rainfall,
               MIN(date) AS first_entry_date,
               MAX(date) AS last_entry_date
        FROM rainfall_log
        WHERE user_id = :userId
        """
    )
    suspend fun getAllTimeStats(userId: String): AllTimeStatsData?

    @Query("SELECT EXISTS(SELECT 1 FROM rainfall_log WHERE user_id = :userId AND date = :date)")
    suspend fun hasEntry(userId: String, date: LocalDate): Boolean

    @Query("SELECT COUNT(*) FROM rainfall_log WHERE user_id = :userId")
    suspend fun getEntryCount(userId: String): Int

    @Query("SELECT * FROM rainfall_log WHERE user_id = :userId ORDER BY date DESC, timestamp DESC LIMIT :limit")
    fun observeRecentEntries(userId: String, limit: Int): Flow<List<RainfallLogEntity>>
}
