package com.example.tateti_20.data.network.model.receive

import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.google.firebase.firestore.DocumentReference

class GameModelDataReceive(
    val hallName: String? = null,

    val board: List<Int?>? = null,

    val isPublic:Boolean? = null,
    val password:String? = null ,

    val isFinished:Boolean? = null,
    val isVisible:Boolean? = null,
    val winner:Int? = null,

    val player1: PlayerModelDataReceive? = null,
    val player2: PlayerModelDataReceive? = null,
    val playerTurn: PlayerModelDataReceive? = null,

)

data class PlayerModelDataReceive(
    val user: DocumentReference? = null,

    val playerType:Int? = null,
    val victories:Int? = null,
    val resetGame: Boolean? = null
) {
}