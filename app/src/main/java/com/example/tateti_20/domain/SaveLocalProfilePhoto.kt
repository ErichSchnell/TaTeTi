package com.example.tateti_20.domain

import android.net.Uri
import javax.inject.Inject

class SaveLocalProfilePhoto @Inject constructor(private val databaseLocalService: DatabaseLocalService) {
    suspend operator fun invoke(uriProfilePhoto: Uri) {
        databaseLocalService.saveProfilePhoto(uriProfilePhoto)
    }


}