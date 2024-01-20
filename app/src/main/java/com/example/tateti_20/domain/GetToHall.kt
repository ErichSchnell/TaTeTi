package com.example.tateti_20.domain

import com.example.tateti_20.ui.model.GameModelUi
import javax.inject.Inject


class GetToHall @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(gameId:String):GameModelUi {
        return dataServer.getToHall(gameId)
    }
}