package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.BoardModelUi
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.HallsModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi

data class HallsModelData (
    val halls: List<GameModelData?>? = null,
) {
    fun toModelUi() = HallsModelUi (
        halls = halls?.map{ it?.toModelUi() } ?: mutableListOf()
    )
}
data class GameModelData (
    val hallId: String? = null,
    val hallName: String? = null,
    val available: Boolean? = null,
    val board: List<Int?>? = null,
    val player1: PlayerModelData? = null,
    val player2: PlayerModelData? = null,
    val playerTurn: PlayerModelData? = null
) {
    fun toModelUi(): GameModelUi {
        return GameModelUi(
            hallId = hallId.orEmpty(),
            hallName = hallName.orEmpty(),
            available = available ?: false,
            board = board?.map { PlayerType.getPlayerById(it) } ?: mutableListOf(),
            player1 = player1?.toModelUi(),
            playerTurn = playerTurn?.toModelUi(),
            player2 = player2?.toModelUi()
        )
    }
}

data class BoardModelData(
    val board: List<Int?>? = null
){
    fun toModelUi() = BoardModelUi(
        board = board?.map { PlayerType.getPlayerById(it) } ?: mutableListOf()
    )
}


data class PlayerModelData(
    val userId: String? = null,
    val nickname:String? = null,
    val playerType:Int? = null,
    val victories:Int? = null,
    val resetGame: Boolean? = null
) {
    fun toModelUi(): PlayerModelUi {
        return PlayerModelUi(
            userId = userId.orEmpty(),
            nickname = nickname.orEmpty(),
            playerType = PlayerType.getPlayerById(playerType ?: 0),
            victories = victories ?: 0,
            resetGame = resetGame ?: false
        )
    }
}