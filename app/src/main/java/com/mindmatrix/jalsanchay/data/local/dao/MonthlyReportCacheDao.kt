package com.mindmatrix.jalsanchay.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mindmatrix.jalsanchay.data.local.entity.MonthlyReportCacheEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MonthlyReportCacheDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(report: MonthlyReportCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(report: MonthlyReportCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiple(reports: List<MonthlyReportCacheEntity>)

    @Update
    suspend fun update(report: MonthlyReportCacheEntity)

    @Query(
        """
        UPDATE monthly_report_cache
        SET insight_text = :insightText,
            generated_at = :generatedAt
        WHERE user_id = :userId AND year = :year AND month = :month
        """
    )
    suspend fun updateInsight(
        userId: String,
        year: Int,
        month: Int,
        insightText: String,
        generatedAt: LocalDateTime
    )

    @Delete
    suspend fun delete(report: MonthlyReportCacheEntity)

    @Query("DELETE FROM monthly_report_cache WHERE user_id = :userId AND year = :year AND month = :month")
    suspend fun deleteMonthReport(userId: String, year: Int, month: Int)

    @Query("SELECT * FROM monthly_report_cache WHERE user_id = :userId AND year = :year AND month = :month LIMIT 1")
    suspend fun getMonthReport(userId: String, year: Int, month: Int): MonthlyReportCacheEntity?

    @Query("SELECT * FROM monthly_report_cache WHERE user_id = :userId AND year = :year AND month = :month LIMIT 1")
    fun observeMonthReport(userId: String, year: Int, month: Int): Flow<MonthlyReportCacheEntity?>

    @Query("SELECT * FROM monthly_report_cache WHERE user_id = :userId ORDER BY year DESC, month DESC LIMIT :limit")
    suspend fun getLastMonths(userId: String, limit: Int): List<MonthlyReportCacheEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM monthly_report_cache WHERE user_id = :userId AND year = :year AND month = :month)")
    suspend fun hasReport(userId: String, year: Int, month: Int): Boolean
}
