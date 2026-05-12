package com.mindmatrix.jalsanchay.data.repository

import com.mindmatrix.jalsanchay.data.local.dao.RainfallLogDao
import com.mindmatrix.jalsanchay.data.local.dao.UserInfrastructureDao
import com.mindmatrix.jalsanchay.data.local.entity.RoofMaterial
import com.mindmatrix.jalsanchay.data.local.entity.UserInfrastructure
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userInfrastructureDao: UserInfrastructureDao,
    private val rainfallLogDao: RainfallLogDao
) {
    suspend fun saveInfrastructure(
        userId: String,
        roofArea: Double,
        tankCapacity: Double,
        roofMaterial: RoofMaterial,
        location: String?,
        personCount: Int
    ) {
        val infrastructure = UserInfrastructure(
            userId = userId,
            roofArea = roofArea,
            tankCapacity = tankCapacity,
            roofMaterial = roofMaterial,
            location = location,
            personCount = personCount
        )
        require(infrastructure.isValid()) { "Infrastructure data is outside supported ranges." }
        userInfrastructureDao.upsert(infrastructure)
    }

    suspend fun getInfrastructure(userId: String): UserInfrastructure? {
        return userInfrastructureDao.getByUserId(userId)
    }

    fun observeInfrastructure(userId: String): Flow<UserInfrastructure?> {
        return userInfrastructureDao.observeByUserId(userId)
    }

    fun observeAllUsers(): Flow<List<UserInfrastructure>> {
        return userInfrastructureDao.observeAllUsers()
    }

    fun observeUserCount(): Flow<Int> {
        return userInfrastructureDao.observeUserCount()
    }

    suspend fun hasSetup(userId: String): Boolean {
        return userInfrastructureDao.hasSetup(userId)
    }

    suspend fun deleteUserAccount(userId: String) {
        rainfallLogDao.deleteByUserId(userId)
        userInfrastructureDao.deleteByUserId(userId)
    }
}
