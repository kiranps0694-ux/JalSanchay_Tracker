package com.mindmatrix.jalsanchay.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "dark_mode_enabled")
    val darkModeEnabled: Boolean = false,
    @ColumnInfo(name = "notifications_enabled")
    val notificationsEnabled: Boolean = true,
    val language: String = "en",
    @ColumnInfo(name = "unit_measurement")
    val unitMeasurement: String = "metric",
    @ColumnInfo(name = "last_synced")
    val lastSynced: LocalDateTime? = null
)
