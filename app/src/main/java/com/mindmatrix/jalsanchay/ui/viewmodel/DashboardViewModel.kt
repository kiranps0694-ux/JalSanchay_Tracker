package com.mindmatrix.jalsanchay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.jalsanchay.data.local.entity.RainfallLogEntity
import com.mindmatrix.jalsanchay.data.local.entity.RoofMaterial
import com.mindmatrix.jalsanchay.data.local.entity.UserInfrastructure
import com.mindmatrix.jalsanchay.data.repository.RainfallRepository
import com.mindmatrix.jalsanchay.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

data class DashboardUiState(
    val activeUserId: String = "",
    val hasSetup: Boolean = false,
    val roofArea: Double = 0.0,
    val tankCapacity: Double = 0.0,
    val roofMaterial: RoofMaterial = RoofMaterial.CONCRETE,
    val todayLitres: Double = 0.0,
    val monthlyLitres: Double = 0.0,
    val totalLitres: Double = 0.0,
    val householdDays: Double = 0.0,
    val recentEntries: List<RainfallLogEntity> = emptyList(),
    val totalUsers: Int = 0,
    val allUsersTotalLitres: Double = 0.0,
    val allUsersMonthlyLitres: Double = 0.0,
    val allUsersEntryCount: Int = 0,
    val allUsers: List<UserInfrastructure> = emptyList(),
    val message: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val rainfallRepository: RainfallRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val message = MutableStateFlow<String?>(null)
    private val activeUserId = MutableStateFlow(DEFAULT_USER_ID)

    private val currentUserData = combine(
        activeUserId.flatMapLatest { userRepository.observeInfrastructure(it) },
        activeUserId.flatMapLatest { rainfallRepository.observeTodayTotalVolume(it) },
        activeUserId.flatMapLatest { rainfallRepository.observeMonthlyTotalVolume(it, YearMonth.now()) },
        activeUserId.flatMapLatest { rainfallRepository.observeAllTimeTotalVolume(it) },
        activeUserId.flatMapLatest { rainfallRepository.observeRecentEntries(it) }
    ) { infrastructure, today, month, total, recent ->
        DashboardUiState(
            activeUserId = infrastructure?.userId ?: activeUserId.value,
            hasSetup = infrastructure != null,
            roofArea = infrastructure?.roofArea ?: 0.0,
            tankCapacity = infrastructure?.tankCapacity ?: 0.0,
            roofMaterial = infrastructure?.roofMaterial ?: RoofMaterial.CONCRETE,
            todayLitres = today,
            monthlyLitres = month,
            totalLitres = total,
            householdDays = total / LITRES_PER_PERSON_PER_DAY / DEFAULT_PERSON_COUNT,
            recentEntries = recent
        )
    }

    private val globalData = combine(
        userRepository.observeUserCount(),
        rainfallRepository.observeGlobalTotalVolume(),
        rainfallRepository.observeGlobalMonthlyTotalVolume(YearMonth.now()),
        rainfallRepository.observeGlobalEntryCount(),
        userRepository.observeAllUsers()
    ) { totalUsers, globalTotal, globalMonth, globalEntries, users ->
        GlobalDashboardData(
            totalUsers = totalUsers,
            allUsersTotalLitres = globalTotal,
            allUsersMonthlyLitres = globalMonth,
            allUsersEntryCount = globalEntries,
            allUsers = users
        )
    }

    private val dashboardData = combine(currentUserData, globalData) { userData, global ->
        userData.copy(
            totalUsers = global.totalUsers,
            allUsersTotalLitres = global.allUsersTotalLitres,
            allUsersMonthlyLitres = global.allUsersMonthlyLitres,
            allUsersEntryCount = global.allUsersEntryCount,
            allUsers = global.allUsers
        )
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        dashboardData,
        message
    ) { data, currentMessage ->
        data.copy(message = currentMessage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DashboardUiState()
    )

    fun saveSetup(
        userIdText: String,
        roofAreaText: String,
        tankCapacityText: String,
        material: RoofMaterial,
        location: String
    ): Boolean {
        val userId = userIdText.trim()
        if (userId.isBlank()) {
            message.update { "Enter your name." }
            return false
        }
        val roofArea = roofAreaText.toDoubleOrNull()
        val tankCapacity = tankCapacityText.toDoubleOrNull()
        if (roofArea == null || tankCapacity == null) {
            message.update { "Enter valid roof area and tank capacity." }
            return false
        }
        if (roofArea !in 10.0..10_000.0 || tankCapacity !in 100.0..100_000.0) {
            message.update { "Use roof area 10-10000 sq ft and tank capacity 100-100000 L." }
            return false
        }

        viewModelScope.launch {
            runCatching {
                userRepository.saveInfrastructure(
                    userId = userId,
                    roofArea = roofArea,
                    tankCapacity = tankCapacity,
                    roofMaterial = material,
                    location = location.takeIf { it.isNotBlank() },
                    personCount = DEFAULT_PERSON_COUNT
                )
            }.onSuccess {
                message.update { "Setup saved. Start adding rainfall." }
            }.onFailure { throwable ->
                message.update { throwable.message ?: "Could not save setup." }
            }
        }
        activeUserId.value = userId
        return true
    }

    fun addRainfall(rainfallText: String, notes: String): Boolean {
        val rainfallMm = rainfallText.toDoubleOrNull()
        if (rainfallMm == null) {
            message.update { "Enter rainfall in millimeters." }
            return false
        }
        if (rainfallMm !in 0.1..1_000.0) {
            message.update { "Rainfall must be between 0.1 and 1000 mm." }
            return false
        }

        viewModelScope.launch {
            val userId = activeUserId.value
            if (!userRepository.hasSetup(userId)) {
                message.update { "Complete setup before adding rainfall." }
                return@launch
            }
            runCatching {
                rainfallRepository.addRainfallLog(
                    userId = userId,
                    rainfallMm = rainfallMm,
                    notes = notes
                )
            }.onSuccess {
                message.update { "Rainfall entry saved." }
            }.onFailure { throwable ->
                message.update { throwable.message ?: "Could not save rainfall entry." }
            }
        }
        return true
    }

    fun saveSetupAndRainfall(
        nameText: String,
        roofAreaText: String,
        tankCapacityText: String,
        material: RoofMaterial,
        rainfallText: String,
        notes: String
    ): Boolean {
        val userId = nameText.trim()
        val roofArea = roofAreaText.toDoubleOrNull()
        val tankCapacity = tankCapacityText.toDoubleOrNull()
        val rainfallMm = rainfallText.toDoubleOrNull()

        when {
            userId.isBlank() -> {
                message.update { "Enter your name." }
                return false
            }
            roofArea == null || tankCapacity == null -> {
                message.update { "Enter valid roof area and tank capacity." }
                return false
            }
            roofArea !in 10.0..10_000.0 || tankCapacity !in 100.0..100_000.0 -> {
                message.update { "Use roof area 10-10000 sq ft and tank capacity 100-100000 L." }
                return false
            }
            rainfallMm == null -> {
                message.update { "Enter rainfall in millimeters." }
                return false
            }
            rainfallMm !in 0.1..1_000.0 -> {
                message.update { "Rainfall must be between 0.1 and 1000 mm." }
                return false
            }
        }

        val validRoofArea = requireNotNull(roofArea)
        val validTankCapacity = requireNotNull(tankCapacity)
        val validRainfallMm = requireNotNull(rainfallMm)

        viewModelScope.launch {
            runCatching {
                userRepository.saveInfrastructure(
                    userId = userId,
                    roofArea = validRoofArea,
                    tankCapacity = validTankCapacity,
                    roofMaterial = material,
                    location = null,
                    personCount = DEFAULT_PERSON_COUNT
                )
                activeUserId.value = userId
                rainfallRepository.addRainfallLog(
                    userId = userId,
                    rainfallMm = validRainfallMm,
                    notes = notes
                )
            }.onSuccess {
                message.update { "Data saved. Dashboard updated." }
            }.onFailure { throwable ->
                message.update { throwable.message ?: "Could not save data." }
            }
        }
        return true
    }

    companion object {
        private const val DEFAULT_USER_ID = ""
        private const val DEFAULT_PERSON_COUNT = 4
        private const val LITRES_PER_PERSON_PER_DAY = 135.0
    }
}

private data class GlobalDashboardData(
    val totalUsers: Int,
    val allUsersTotalLitres: Double,
    val allUsersMonthlyLitres: Double,
    val allUsersEntryCount: Int,
    val allUsers: List<UserInfrastructure>
)
