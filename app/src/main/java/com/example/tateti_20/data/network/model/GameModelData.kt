package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class GameModelData (
    val hallName: String? = null,

    val board: List<Int?>? = null,

    val player1: PlayerModelData? = null,
    val player2: PlayerModelData? = null,
    val playerTurn: PlayerModelData? = null,

    @field:JvmField val isPublic:Boolean? = null,
    val password:String? = null,

    @field:JvmField val isFinished:Boolean? = null,
    @field:JvmField val isVisible:Boolean? = null,
    val winner:Int? = null
) {
    constructor(): this(null,null,null,null,null,null,null,null,null,null,)
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
            winner = winner ?: 0
        )
    }
}

@Keep
data class PlayerModelData(
    val userId: String? = null,
    val userName: String? = null,

    val playerType:Int? = null,
    val victories:Int? = null,
    val resetGame: Boolean? = null
) {
    fun toModelUi(): PlayerModelUi {
        return PlayerModelUi(
            userId = userId.orEmpty(),
            userName = userName.orEmpty(),
            playerType = PlayerType.getPlayerById(playerType ?: 0),
            victories = victories ?: 0,
            resetGame = resetGame ?: false
        )
    }
}