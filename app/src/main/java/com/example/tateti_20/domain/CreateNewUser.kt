package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.ui.model.UserModelUi
import javax.inject.Inject

class CreateNewUser  @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(userModelUi: UserModelUi): Boolean {
        val userModelData = userModelUi.toModelData()

        return dataServer.createUser(userModelData)
    }
}