package com.mindmatrix.jalsanchay.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "monthly_report_cache",
    foreignKeys = [
        ForeignKey(
            entity = UserInfrastructure::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["user_id", "year", "month"], unique = true)
    ]
)
data class MonthlyReportCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: String,
    val year: Int,
    val month: Int,
    @ColumnInfo(name = "total_litres")
    val totalLitres: Double,
    @ColumnInfo(name = "household_days")
    val householdDays: Double,
    @ColumnInfo(name = "rain_days")
    val rainDays: Int,
    @ColumnInfo(name = "insight_text")
    val insightText: String? = null,
    @ColumnInfo(name = "generated_at")
    val generatedAt: LocalDateTime = LocalDateTime.now()
)
