package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import net.bytebuddy.utility.RandomString
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class RequestStatusEntityMapperTest {
    @Test
    fun mapToDomain() {

        val requestStatusEntity = RequestStatusEntity(
            Random().nextBoolean(),
            RandomString.make()
        )
        val requestStatusEntityMapper = RequestStatusEntityMapper()
        val requestStatus = requestStatusEntityMapper.mapToDomain(requestStatusEntity)

        assertEquals(requestStatus.success, requestStatusEntity.success)
        assertEquals(requestStatus.message, requestStatusEntity.message)
    }
}