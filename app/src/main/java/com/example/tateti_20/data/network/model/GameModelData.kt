package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.BoardModelUi
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.HallsModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType

data class HallsModelData (
    val halls: List<GameModelData?>? = null,
) {
    fun toModelUi() = HallsModelUi (
        halls = halls?.map{ it?.toModelUi("") } ?: mutableListOf()
    )
}
data class GameModelData (
    val hallName: String? = null,

    val board: List<Int?>? = null,

    val player1: PlayerModelData? = null,
    val player2: PlayerModelData? = null,
    val playerTurn: PlayerModelData? = null,

    val isPublic:Boolean? = null,
    val password:String? = null,

    val isFinished:Boolean? = null,
    val isVisible:Boolean? = null,
    val winner:Int? = null,
) {
    fun toModelUi(hallId: String): GameModelUi {
        return GameModelUi(
            hallId = hallId,
            hallName = hallName.orEmpty(),

            board = board?.map { PlayerType.getPlayerById(it) } ?: mutableListOf(),

            player1 = player1?.toModelUi(),
            player2 = player2?.toModelUi(),
            playerTurn = playerTurn?.toModelUi(),

            isPublic = isPublic ?: false,
            password = password ?: "0000",

            isFinished = isFinished ?: true,
            isVisible = isVisible ?: false,
            winner = winner ?: 0,
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

    val playerType:Int? = null,
    val victories:Int? = null,
    val resetGame: Boolean? = null
) {
    fun toModelUi(): PlayerModelUi {
        return PlayerModelUi(
            userId = userId.orEmpty(),
            playerType = PlayerType.getPlayerById(playerType ?: 0),
            victories = victories ?: 0,
            resetGame = resetGame ?: false
        )
    }
}