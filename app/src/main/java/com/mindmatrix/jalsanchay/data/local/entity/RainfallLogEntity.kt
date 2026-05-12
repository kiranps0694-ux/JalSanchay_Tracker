package com.mindmatrix.jalsanchay.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "rainfall_log",
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
        Index(value = ["date"]),
        Index(value = ["user_id", "date"])
    ]
)
data class RainfallLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "rainfall_mm")
    val rainfallMm: Double,
    @ColumnInfo(name = "volume_calculated")
    val volumeCalculated: Double,
    val date: LocalDate = LocalDate.now(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null
) {
    fun isValid(): Boolean {
        return userId.isNotBlank() &&
            rainfallMm in 0.1..1_000.0 &&
            volumeCalculated >= 0.0
    }

    fun getVolumeString(): String = "%.1f L".format(volumeCalculated)

    fun getRainfallString(): String = "%.1f mm".format(rainfallMm)
}
