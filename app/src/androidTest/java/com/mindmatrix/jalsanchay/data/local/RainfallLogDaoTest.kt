package com.mindmatrix.jalsanchay.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mindmatrix.jalsanchay.data.local.entity.RainfallLogEntity
import com.mindmatrix.jalsanchay.data.local.entity.RoofMaterial
import com.mindmatrix.jalsanchay.data.local.entity.UserInfrastructure
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class RainfallLogDaoTest {
    private lateinit var database: JalSanchayDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, JalSanchayDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getTodayTotalVolume_returnsInsertedRainfallTotal() = runBlocking {
        database.userInfrastructureDao().upsert(
            UserInfrastructure(
                userId = "user123",
                roofArea = 40.0,
                tankCapacity = 2_000.0,
                roofMaterial = RoofMaterial.CONCRETE
            )
        )

        database.rainfallLogDao().insert(
            RainfallLogEntity(
                userId = "user123",
                rainfallMm = 10.0,
                volumeCalculated = 320.0,
                date = LocalDate.now()
            )
        )

        assertEquals(320.0, database.rainfallLogDao().getTodayTotalVolume("user123", LocalDate.now()), 0.01)
    }
}
