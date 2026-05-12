package com.mindmatrix.jalsanchay.data.local.model

import androidx.room.ColumnInfo
import java.time.LocalDate

data class DailyAggregateData(
    val date: LocalDate,
    @ColumnInfo(name = "daily_volume")
    val dailyVolume: Double,
    @ColumnInfo(name = "entry_count")
    val entryCount: Int
)
