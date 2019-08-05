package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import com.farhad.sparkeditableprofile.domain.model.RequestStatus

class RequestStatusEntityMapper{
    fun mapToDomain(requestStatusEntity: RequestStatusEntity) =
        RequestStatus(requestStatusEntity.success, requestStatusEntity.message)
}
