package com.example.tateti_20.domain

import com.example.tateti_20.ui.model.BoardModelUi
import com.example.tateti_20.ui.model.PlayerType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JoinToBoard  @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(gameId:String){}
//    : Flow<BoardModelUi?> {
//        return dataServerService.joinToBoard(gameId)
//    }

}