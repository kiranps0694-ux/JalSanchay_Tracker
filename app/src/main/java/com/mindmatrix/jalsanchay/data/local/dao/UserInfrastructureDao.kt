package com.mindmatrix.jalsanchay.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mindmatrix.jalsanchay.data.local.entity.RoofMaterial
import com.mindmatrix.jalsanchay.data.local.entity.UserInfrastructure
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface UserInfrastructureDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(infrastructure: UserInfrastructure): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(infrastructure: UserInfrastructure)

    @Update
    suspend fun update(infrastructure: UserInfrastructure)

    @Query(
        """
        UPDATE user_infrastructure
        SET roof_area = :roofArea,
            tank_capacity = :tankCapacity,
            roof_material = :roofMaterial,
            runoff_coefficient = :runoffCoefficient,
            location = :location,
            person_count = :personCount,
            updated_at = :updatedAt
        WHERE user_id = :userId
        """
    )
    suspend fun updateInfrastructure(
        userId: String,
        roofArea: Double,
        tankCapacity: Double,
        roofMaterial: RoofMaterial,
        runoffCoefficient: Double,
        location: String?,
        personCount: Int,
        updatedAt: LocalDateTime
    )

    @Delete
    suspend fun delete(infrastructure: UserInfrastructure)

    @Query("DELETE FROM user_infrastructure WHERE user_id = :userId")
    suspend fun deleteByUserId(userId: String)

    @Query("SELECT * FROM user_infrastructure WHERE user_id = :userId LIMIT 1")
    suspend fun getByUserId(userId: String): UserInfrastructure?

    @Query("SELECT * FROM user_infrastructure WHERE user_id = :userId LIMIT 1")
    fun observeByUserId(userId: String): Flow<UserInfrastructure?>

    @Query("SELECT * FROM user_infrastructure ORDER BY updated_at DESC")
    fun observeAllUsers(): Flow<List<UserInfrastructure>>

    @Query("SELECT COUNT(*) FROM user_infrastructure")
    fun observeUserCount(): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM user_infrastructure WHERE user_id = :userId)")
    suspend fun hasSetup(userId: String): Boolean
}
