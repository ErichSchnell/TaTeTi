package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.ui.model.UserModelUi
import javax.inject.Inject

class UpdateUser @Inject constructor(private val dataServerService: DataServerService) {
    suspend operator fun invoke(userModelUi: UserModelUi):Boolean {
        return dataServerService.updateUser(userModelUi.userId,userModelUi.toModelData())
    }

}