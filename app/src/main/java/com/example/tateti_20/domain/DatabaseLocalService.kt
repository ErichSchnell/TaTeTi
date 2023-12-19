package com.example.tateti_20.domain

import kotlinx.coroutines.flow.Flow

interface DatabaseLocalService {

    suspend fun saveUserId(userId: String)
    fun getUserId(): Flow<String>

}