package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.receive.GameModelDataReceive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class JoinToHall @Inject constructor(private val dataServerService: DataServerService) {
    suspend operator fun invoke(gameId:String) : Flow<GameModelData>{//
        Log.i("erich", "invoke gameId: $gameId")
        return dataServerService.joinToHall(gameId)
    }
//    : Flow<GameModelUi?> {
//       return dataServerService.joinToGame(gameId)
//    }
}