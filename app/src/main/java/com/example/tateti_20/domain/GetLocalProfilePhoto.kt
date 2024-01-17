package com.example.tateti_20.domain

import android.net.Uri
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLocalProfilePhoto @Inject constructor(private val databaseLocalService: DatabaseLocalService) {
    suspend operator fun invoke(): Uri = databaseLocalService.getProfilePhoto().first()
}