package com.mindmatrix.jalsanchay.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "user_infrastructure",
    indices = [
        Index(value = ["user_id"], unique = true),
        Index(value = ["created_at"])
    ]
)
data class UserInfrastructure(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "roof_area")
    val roofArea: Double,
    @ColumnInfo(name = "tank_capacity")
    val tankCapacity: Double,
    @ColumnInfo(name = "roof_material")
    val roofMaterial: RoofMaterial,
    @ColumnInfo(name = "runoff_coefficient")
    val runoffCoefficient: Double = roofMaterial.runoffCoefficient,
    val location: String? = null,
    @ColumnInfo(name = "person_count")
    val personCount: Int = 4,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun isValid(): Boolean {
        return userId.isNotBlank() &&
            roofArea in 10.0..10_000.0 &&
            tankCapacity in 100.0..100_000.0 &&
            runoffCoefficient in 0.0..1.0 &&
            personCount in 1..50
    }

    fun getRoofMaterialDisplayName(): String = roofMaterial.displayName

    fun calculateHarvestedLitres(rainfallMm: Double): Double {
        return roofArea * rainfallMm * 0.0299 * runoffCoefficient
    }
}
