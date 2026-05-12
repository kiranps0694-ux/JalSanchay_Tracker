package com.mindmatrix.jalsanchay.data.local.model

import androidx.room.ColumnInfo
import java.time.LocalDate

data class AllTimeStatsData(
    @ColumnInfo(name = "total_entries")
    val totalEntries: Int,
    @ColumnInfo(name = "rain_days")
    val rainDays: Int,
    @ColumnInfo(name = "total_volume")
    val totalVolume: Double,
    @ColumnInfo(name = "max_rainfall")
    val maxRainfall: Double,
    @ColumnInfo(name = "first_entry_date")
    val firstEntryDate: LocalDate?,
    @ColumnInfo(name = "last_entry_date")
    val lastEntryDate: LocalDate?
)
