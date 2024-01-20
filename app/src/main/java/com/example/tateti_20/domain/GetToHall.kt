package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.R
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import javax.inject.Inject

val TAG = R.string.log.toString()

class GetToHall @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(gameId:String):GameModelUi {

        /*val currentGame = dataServer.getToHall(gameId)
        Log.i(TAG, "invoke currentGame: $currentGame")
        val currentUser1 = dataServer.getUser(currentGame.player1?.userId.orEmpty())
        Log.i(TAG, "invoke currentUser1: $currentUser1")
        val currentUser2 = dataServer.getUser(currentGame.player1?.userId.orEmpty())
        Log.i(TAG, "invoke currentUser2: $currentUser2")

        val player1 = currentGame.player1?.copy(user = currentUser1)
        Log.i(TAG, "invoke player1: $player1")

        val player2 = currentGame.player2?.copy(user = currentUser2)
        Log.i(TAG, "invoke player2: $player2")

        val playerTurn = currentGame.playerTurn?.let {
            if (it.userId == player1?.userId) {
                player1
            } else {
                player2
            }
        }
        Log.i(TAG, "invoke playerTurn: $playerTurn")*/

        return dataServer.getToHall(gameId)
        /*currentGame.copy(
            player1 = player1,
            player2 = player2,
            playerTurn = playerTurn
        )*/
    }
}