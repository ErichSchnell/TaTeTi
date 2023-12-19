package com.example.tateti_20.domain

import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JoinToPlayer @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(gameId:String, playerType: PlayerType){}
//    : Flow<PlayerModelUi?> {
//        return dataServerService.joinToPlayer(gameId,playerType)
//    }
}