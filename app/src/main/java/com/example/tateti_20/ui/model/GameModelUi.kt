package com.example.tateti_20.ui.model

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData


data class GameModelUi(
    val hallId: String?,
    val hallName: String,

    val board: List<PlayerType>,

    val player1: PlayerModelUi?,
    val player2: PlayerModelUi?,
    val playerTurn: PlayerModelUi?,

    val isPublic:Boolean,
    val password:String,

    val isFinished:Boolean,
    val isVisible:Boolean,
    val winner:Int,

    val isGameReady: Boolean = false,
    val isMyTurn: Boolean = false
) {
    fun toModelData(): GameModelData {
        return GameModelData(
            hallName = hallName,

            board = board.map {it.id},

            player1 = player1?.toModelData(),
            player2 = player2?.toModelData(),
            playerTurn = playerTurn?.toModelData(),

            isPublic = isPublic,
            password = password,

            isFinished = isFinished,
            isVisible = isVisible,
            winner = winner
        )
    }
    fun rstBoard(value: Boolean = false) = this.copy(board = List(9) {PlayerType.Empty})
}


data class PlayerModelUi(
    val userId:String = "",
    val userName:String = "",

    val playerType: PlayerType = PlayerType.Empty,
    val victories: Int = 0,
    val resetGame: Boolean = false
) {
    fun toModelData() = PlayerModelData(
        userId = userId,
        userName = userName,
        playerType = playerType.id,
        victories = victories,
        resetGame = resetGame
    )
    fun rstGame(value: Boolean = false): PlayerModelUi {
        return this.copy(resetGame = value)
    }
    fun incVictory() = this.copy(victories = victories.inc())

}

sealed class PlayerType(val id: Int, val simbol: String){
    object FirstPlayer:PlayerType(2,"X")
    object SecondPlayer:PlayerType(3,"O")
    object Empty:PlayerType(0,"")

    companion object{

        fun getPlayerById(id: Int?): PlayerType{
            return when(id){
                FirstPlayer.id -> FirstPlayer
                SecondPlayer.id -> SecondPlayer
                else -> Empty
            }
        }
    }
}

sealed class GameModelUiNames(val valueName: String){
    object HALL_ID:GameModelUiNames("hallId")
    object HALL_NAME:GameModelUiNames("hallName")
    object BOARD:GameModelUiNames("board")
    object PLAYER_1:GameModelUiNames("player1")
    object PLAYER_2:GameModelUiNames("player2")
    object PLAYER_TURN:GameModelUiNames("playerTurn")
}

