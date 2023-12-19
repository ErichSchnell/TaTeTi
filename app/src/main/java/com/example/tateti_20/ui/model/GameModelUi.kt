package com.example.tateti_20.ui.model

import com.example.tateti_20.data.network.model.BoardModelData
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.HallsModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData


data class HallsModelUi (
    val halls: List<GameModelUi?>?
) {
    fun toModelData() = HallsModelData(
        halls = halls?.map { it?.toModelData() }
    )
}

data class GameModelUi(
    val hallId: String?,
    val hallName: String,
    val available: Boolean,
    val board: List<PlayerType>,
    val player2: PlayerModelUi?,
    val player1: PlayerModelUi?,

    val isGameReady: Boolean = false,
    val isMyTurn: Boolean = false,
    val playerTurn: PlayerModelUi?,
) {
    fun toModelData(): GameModelData {
        return GameModelData(
            hallId = hallId,
            hallName = hallName,
            available = available,
            board = board.map {it.id},
            player1 = player1?.toModelData(),
            playerTurn = playerTurn?.toModelData(),
            player2 = player2?.toModelData()
        )
    }
    fun rstBoard(value: Boolean = false) = this.copy(board = List(9) {PlayerType.Empty})
}


data class BoardModelUi(
    val board: List<PlayerType>
){
    fun toModelData() = BoardModelData(
        board = board.map {it.id}
    )
}

data class PlayerModelUi(
    val userId: String = "",
    val nickname:String = "",
    val playerType: PlayerType = PlayerType.Empty,
    val victories: Int = 0,
    val resetGame: Boolean = false
) {
    fun toModelData(): PlayerModelData = PlayerModelData(
        userId = userId,
        nickname = nickname,
        playerType = playerType.id,
        victories = victories,
        resetGame = resetGame
    )
    fun rstGame(value: Boolean = false): PlayerModelUi {
        return this.copy(resetGame = value)
    }
    fun incVictory() = this.copy(victories = victories.inc())

}

data class UserModelUi(
    val userId: String = "",
    val nickname:String = "",

    val victories: Int = 0,
    val defeats: Int = 0,

    val hallId: String = "",
) {
    fun toPlayer(playerType: PlayerType): PlayerModelUi = PlayerModelUi(
        userId = userId ,
        nickname = nickname,
        playerType = playerType
    )
    fun toModelData(): UserModelData = UserModelData(
        userId = userId ,
        nickname = nickname,
        victories = victories,
        defeats = defeats,
        hallId = hallId,
    )
    fun incVic():UserModelUi{
        return this.copy(
            victories = victories.inc()
        )
    }
    fun incDef():UserModelUi{
        return this.copy(
            defeats = defeats.inc()
        )
    }
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

