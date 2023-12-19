package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import javax.inject.Inject

class CreateNewGame @Inject constructor(private val dataServer: DataServerService) {
    operator fun invoke(gameModelData: GameModelData): String {
        Log.d("printResume", "invoke gameModelData: $gameModelData")
        return dataServer.createGame(gameModelData)
    }

}