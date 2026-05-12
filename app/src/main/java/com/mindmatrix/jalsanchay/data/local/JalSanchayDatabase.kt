package com.mindmatrix.jalsanchay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mindmatrix.jalsanchay.data.local.dao.MonthlyReportCacheDao
import com.mindmatrix.jalsanchay.data.local.dao.RainfallLogDao
import com.mindmatrix.jalsanchay.data.local.dao.UserInfrastructureDao
import com.mindmatrix.jalsanchay.data.local.entity.AppSettingsEntity
import com.mindmatrix.jalsanchay.data.local.entity.MonthlyReportCacheEntity
import com.mindmatrix.jalsanchay.data.local.entity.RainfallLogEntity
import com.mindmatrix.jalsanchay.data.local.entity.UserInfrastructure

@Database(
    entities = [
        UserInfrastructure::class,
        RainfallLogEntity::class,
        MonthlyReportCacheEntity::class,
        AppSettingsEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class JalSanchayDatabase : RoomDatabase() {
    abstract fun userInfrastructureDao(): UserInfrastructureDao
    abstract fun rainfallLogDao(): RainfallLogDao
    abstract fun monthlyReportCacheDao(): MonthlyReportCacheDao

    companion object {
        const val DATABASE_NAME = "jal_sanchay_db"
    }
}
