package com.example.tateti_20.domain

import com.example.tateti_20.ui.model.GameModelUi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JoinToGame @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(gameId:String){}
//    : Flow<GameModelUi?> {
//       return dataServerService.joinToGame(gameId)
//    }
}