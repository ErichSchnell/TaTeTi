package com.example.tateti_20.domain

import javax.inject.Inject

class SetProfilePhotoState @Inject constructor(private val databaseLocalService: DatabaseLocalService) {
    suspend operator fun invoke(state:Boolean) {
        databaseLocalService.setProfilePhotoState(state)
    }

}