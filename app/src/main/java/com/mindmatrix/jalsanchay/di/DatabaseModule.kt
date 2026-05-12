package com.mindmatrix.jalsanchay.di

import android.content.Context
import androidx.room.Room
import com.mindmatrix.jalsanchay.data.local.JalSanchayDatabase
import com.mindmatrix.jalsanchay.data.local.dao.MonthlyReportCacheDao
import com.mindmatrix.jalsanchay.data.local.dao.RainfallLogDao
import com.mindmatrix.jalsanchay.data.local.dao.UserInfrastructureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): JalSanchayDatabase {
        return Room.databaseBuilder(
            context,
            JalSanchayDatabase::class.java,
            JalSanchayDatabase.DATABASE_NAME
        )
            .enableMultiInstanceInvalidation()
            .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()
    }

    @Provides
    fun provideUserInfrastructureDao(database: JalSanchayDatabase): UserInfrastructureDao {
        return database.userInfrastructureDao()
    }

    @Provides
    fun provideRainfallLogDao(database: JalSanchayDatabase): RainfallLogDao {
        return database.rainfallLogDao()
    }

    @Provides
    fun provideMonthlyReportCacheDao(database: JalSanchayDatabase): MonthlyReportCacheDao {
        return database.monthlyReportCacheDao()
    }
}
