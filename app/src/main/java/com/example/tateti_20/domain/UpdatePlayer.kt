package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.ui.model.PlayerType
import javax.inject.Inject

class UpdatePlayer @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(hallId: String, playerModelData: PlayerModelData){
        dataServerService.updatePlayer(hallId, playerModelData)
    }
}