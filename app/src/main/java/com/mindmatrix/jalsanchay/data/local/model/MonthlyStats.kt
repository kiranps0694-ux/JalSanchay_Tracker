package com.mindmatrix.jalsanchay.data.local.model

import androidx.room.ColumnInfo

data class MonthlyStats(
    @ColumnInfo(name = "entry_count")
    val entryCount: Int,
    @ColumnInfo(name = "rain_days")
    val rainDays: Int,
    @ColumnInfo(name = "avg_rainfall")
    val averageRainfall: Double,
    @ColumnInfo(name = "max_rainfall")
    val maxRainfall: Double,
    @ColumnInfo(name = "min_rainfall")
    val minRainfall: Double,
    @ColumnInfo(name = "total_volume")
    val totalVolume: Double
)
