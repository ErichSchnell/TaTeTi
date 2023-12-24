package com.example.tateti_20.domain

import android.util.Log
import javax.inject.Inject

class JoinToHall @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(gameId:String){
        Log.i("erich", "invoke gameId: $gameId")
        dataServerService.joinToHall(gameId)
    }
//    : Flow<GameModelUi?> {
//       return dataServerService.joinToGame(gameId)
//    }
}