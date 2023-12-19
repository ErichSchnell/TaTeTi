package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.UserModelData
import javax.inject.Inject

class CreateNewUser  @Inject constructor(private val dataServer: DataServerService) {
    operator fun invoke(userModelData: UserModelData) = dataServer.createUser(userModelData)

}