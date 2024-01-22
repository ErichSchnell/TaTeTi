package com.example.tateti_20.domain

import android.net.Uri
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLocalProfilePhotoState @Inject constructor(private val databaseLocalService: DatabaseLocalService) {
    suspend operator fun invoke(): Boolean = databaseLocalService.getProfilePhotoState().first()
}