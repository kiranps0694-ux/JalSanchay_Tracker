package com.mindmatrix.jalsanchay.data.repository

import com.mindmatrix.jalsanchay.data.local.dao.RainfallLogDao
import com.mindmatrix.jalsanchay.data.local.entity.RainfallLogEntity
import com.mindmatrix.jalsanchay.data.local.model.AllTimeStatsData
import com.mindmatrix.jalsanchay.data.local.model.DailyAggregateData
import com.mindmatrix.jalsanchay.data.local.model.MonthlyStats
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class RainfallRepository @Inject constructor(
    private val rainfallLogDao: RainfallLogDao,
    private val userRepository: UserRepository
) {
    suspend fun addRainfallLog(userId: String, rainfallMm: Double, notes: String?): Long {
        val infrastructure = requireNotNull(userRepository.getInfrastructure(userId)) {
            "Please complete infrastructure setup before adding rainfall."
        }
        val volume = infrastructure.calculateHarvestedLitres(rainfallMm)
        val entry = RainfallLogEntity(
            userId = userId,
            rainfallMm = rainfallMm,
            volumeCalculated = volume,
            notes = notes?.takeIf { it.isNotBlank() }
        )
        require(entry.isValid()) { "Rainfall entry is outside supported ranges." }
        return rainfallLogDao.insert(entry)
    }

    fun observeTodayTotalVolume(userId: String): Flow<Double> {
        return rainfallLogDao.observeTodayTotalVolume(userId, LocalDate.now())
    }

    fun observeMonthlyTotalVolume(userId: String, yearMonth: YearMonth): Flow<Double> {
        return rainfallLogDao.observeMonthlyTotalVolume(
            userId = userId,
            startDate = yearMonth.atDay(1),
            endDate = yearMonth.atEndOfMonth()
        )
    }

    fun observeAllTimeTotalVolume(userId: String): Flow<Double> {
        return rainfallLogDao.observeAllTimeTotalVolume(userId)
    }

    fun observeGlobalTotalVolume(): Flow<Double> {
        return rainfallLogDao.observeGlobalTotalVolume()
    }

    fun observeGlobalMonthlyTotalVolume(yearMonth: YearMonth): Flow<Double> {
        return rainfallLogDao.observeGlobalMonthlyTotalVolume(
            startDate = yearMonth.atDay(1),
            endDate = yearMonth.atEndOfMonth()
        )
    }

    fun observeGlobalEntryCount(): Flow<Int> {
        return rainfallLogDao.observeGlobalEntryCount()
    }

    fun observeRecentEntries(userId: String): Flow<List<RainfallLogEntity>> {
        return rainfallLogDao.observeRecentEntries(userId, RECENT_ENTRY_LIMIT)
    }

    suspend fun getLast7DaysData(userId: String): List<DailyAggregateData> {
        return rainfallLogDao.getLast7DaysData(userId, LocalDate.now().minusDays(6))
    }

    suspend fun getMonthlyStats(userId: String, yearMonth: YearMonth): MonthlyStats? {
        return rainfallLogDao.getMonthlyStats(userId, yearMonth.atDay(1), yearMonth.atEndOfMonth())
    }

    suspend fun getAllTimeStats(userId: String): AllTimeStatsData? {
        return rainfallLogDao.getAllTimeStats(userId)
    }

    suspend fun getMonthlyTotalVolume(userId: String, yearMonth: YearMonth): Double {
        return rainfallLogDao.getMonthlyTotalVolume(userId, yearMonth.atDay(1), yearMonth.atEndOfMonth())
    }

    suspend fun getTodayTotalVolume(userId: String, date: LocalDate = LocalDate.now()): Double {
        return rainfallLogDao.getTodayTotalVolume(userId, date)
    }

    private companion object {
        const val RECENT_ENTRY_LIMIT = 20
    }
}
