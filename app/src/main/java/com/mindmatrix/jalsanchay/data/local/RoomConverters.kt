package com.mindmatrix.jalsanchay.data.local

import androidx.room.TypeConverter
import com.mindmatrix.jalsanchay.data.local.entity.RoofMaterial
import java.time.LocalDate
import java.time.LocalDateTime

class RoomConverters {
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let(LocalDateTime::parse)

    @TypeConverter
    fun fromRoofMaterial(value: RoofMaterial?): String? = value?.name

    @TypeConverter
    fun toRoofMaterial(value: String?): RoofMaterial? = value?.let(RoofMaterial::valueOf)
}
