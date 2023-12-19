package com.example.tateti_20.domain

import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLocalUserId @Inject constructor(private val databaseLocalService: DatabaseLocalService) {
    suspend operator fun invoke(): String = databaseLocalService.getUserId().first()


}