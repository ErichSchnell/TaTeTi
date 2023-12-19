package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import javax.inject.Inject

class UpdateGame @Inject constructor(private val dataServerService: DataServerService) {

    operator fun invoke(gameModelData: GameModelData){
        dataServerService.updateGame(gameModelData)
    }

}