package com.goto_delivery.pgoto.domain.repository

import com.goto_delivery.pgoto.domain.model.User
import com.goto_delivery.pgoto.ui.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun updateEmail(newEmail: String): Flow<Resource<Unit>>

    fun updateFullName(newName: String): Flow<Resource<Unit>>

    fun updateAddress(newAddress: String, city: String): Flow<Resource<Unit>>

    fun updatePhoneNumber(newPhoneNumber: String): Flow<Resource<Unit>>

    fun getAccountInfo(): Flow<Resource<User>>
}