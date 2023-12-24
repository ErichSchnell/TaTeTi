package com.example.tateti_20.domain

import com.example.tateti_20.ui.model.UserModelUi
import javax.inject.Inject

class CreateNewUser  @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(userModelUi: UserModelUi): Boolean {
        return dataServer.createUser(userModelUi.userId,userModelUi.toModelData())
    }
}