package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class GameModelData (
    @get:PropertyName("hallName") @PropertyName("hallName") val hallName: String? = null,

    @get:PropertyName("board") @PropertyName("board") val board: List<Int?>? = null,

    @get:PropertyName("player1") @PropertyName("player1") val player1: PlayerModelData? = null,
    @get:PropertyName("player2") @PropertyName("player2") val player2: PlayerModelData? = null,
    @get:PropertyName("playerTurn") @PropertyName("playerTurn") val playerTurn: PlayerModelData? = null,

    @get:PropertyName("isPublic") @PropertyName("isPublic") @field:JvmField val isPublic:Boolean? = null,
    @get:PropertyName("password") @PropertyName("password")  val password:String? = null,

    @get:PropertyName("isFinished") @PropertyName("isFinished") @field:JvmField val isFinished:Boolean? = null,
    @get:PropertyName("isVisible") @PropertyName("isVisible") @field:JvmField val isVisible:Boolean? = null,
    @get:PropertyName("winner") @PropertyName("winner") val winner:Int? = null
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
    @get:PropertyName("userId") @PropertyName("userId") val userId: String? = null,
    @get:PropertyName("userName") @PropertyName("userName") val userName: String? = null,

    @get:PropertyName("playerType") @PropertyName("playerType") val playerType:Int? = null,
    @get:PropertyName("victories") @PropertyName("victories") val victories:Int? = null,
    @get:PropertyName("resetGame") @PropertyName("resetGame") val resetGame: Boolean? = null
) {
    constructor(): this(null,null,null,null,null)

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