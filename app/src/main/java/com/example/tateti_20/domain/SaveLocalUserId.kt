package com.example.tateti_20.domain

import javax.inject.Inject

class SaveLocalUserId @Inject constructor(private val databaseLocalService: DatabaseLocalService) {
    suspend operator fun invoke(userId: String) {
        databaseLocalService.saveUserId(userId)
    }


}