package com.dm.auth.domain

import com.dm.core.domain.util.DataError
import com.dm.core.domain.util.EmptyDataResult

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyDataResult<DataError.Network>
    suspend fun register(email: String, password: String): EmptyDataResult<DataError.Network>
}