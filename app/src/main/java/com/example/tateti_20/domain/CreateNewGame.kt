package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.ui.theme.string_log
import javax.inject.Inject

private const val TAG = string_log
class CreateNewGame @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(gameModelData: GameModelData): String {
        Log.d(TAG, "invoke gameModelData: $gameModelData")
        return dataServer.createHall(gameModelData)
    }

}