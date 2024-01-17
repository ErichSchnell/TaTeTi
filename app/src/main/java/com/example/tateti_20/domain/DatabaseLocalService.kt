package com.example.tateti_20.domain

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface DatabaseLocalService {

    suspend fun saveUserId(userId: String)
    fun getUserId(): Flow<String>

    suspend fun saveProfilePhoto(uriImage: Uri)
    fun getProfilePhoto(): Flow<Uri>

}