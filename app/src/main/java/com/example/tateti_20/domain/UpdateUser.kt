package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.UserModelData
import javax.inject.Inject

class UpdateUser @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(userModelData: UserModelData) {
        dataServerService.updateUser(userModelData)
    }

}