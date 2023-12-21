package com.example.tateti_20.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.tateti_20.domain.JoinToBoard
//import com.example.tateti_20.domain.JoinToGame
//import com.example.tateti_20.domain.JoinToPlayer
//import com.example.tateti_20.domain.JoinToUser
//import com.example.tateti_20.domain.UpdateBoard
//import com.example.tateti_20.domain.UpdateGame
//import com.example.tateti_20.domain.UpdatePlayer
//import com.example.tateti_20.domain.UpdateUser
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = "erich"

@HiltViewModel
class GameViewModel @Inject constructor(
//    private val joinToGame: JoinToGame,
//    private val updateGame: UpdateGame,
//
//    private val joinToBoard: JoinToBoard,
//    private val updateBoard: UpdateBoard,
//
//    private val joinToPlayer: JoinToPlayer,
//    private val updatePlayer: UpdatePlayer,
//
//    private val updateUser: UpdateUser,
//    private val joinToUser: JoinToUser
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameViewState>(GameViewState.LOADING)
    val uiState: StateFlow<GameViewState> = _uiState

    private val _game = MutableStateFlow<GameModelUi?>(null)
    val game: StateFlow<GameModelUi?> = _game

    private val _winner = MutableStateFlow<PlayerType>(PlayerType.Empty)
    val winner: StateFlow<PlayerType> = _winner

    private val _user = MutableStateFlow<UserModelUi?>(UserModelUi())
    val user: StateFlow<UserModelUi?> = _user

    private val _victories = MutableStateFlow<PlayerVictories>(PlayerVictories())
    val victories: StateFlow<PlayerVictories> = _victories


    /*
    *   USER
    */
    fun initGame(userId: String, hallId: String) {
        viewModelScope.launch {
//            joinToUser(userId).collect {
//                if (it != null) {
//                    _user.value = it
//                    printResumeUser()
//
//                    if (_uiState.value == GameViewState.LOADING) joinToGameInit(hallId)
//                    Log.d(TAG, "initGame: fin")
//                }
//            }
        }
    }


    /*
    *   GAME
    */
    fun joinToGameInit(hallId: String) {
        if (_user.value?.lastHall != hallId) joinLikeGuest(hallId)

        join(hallId)
    }
    private fun joinLikeGuest(hallId: String) {
        viewModelScope.launch {
//            joinToGame(hallId).take(1).collect {
//                if (it != null) {
//                    _game.value = it
//
//                    if (_user.value?.hallId == it.hallId) {
//                        val currentPlayer = it.player1?.copy(nickname = _user.value?.nickname ?: "",)
//                        updatePlayer(hallId,currentPlayer?.toModelData() ?: PlayerModelData())
//                    } else {
//                        var currentPlayer = if(it.player2 != null){
//                            it.player2.copy(
//                                userId = _user.value?.userId ?: "",
//                                nickname = _user.value?.nickname ?: ""
//                            )
//                        } else {
//                            _user.value?.toPlayer(PlayerType.SecondPlayer)
//                        }
//                        updateGame(it.copy(available = false).toModelData())
//                        updatePlayer(hallId,currentPlayer?.toModelData() ?: PlayerModelData())
//                    }
//                }
//            }
        }
    }
    private fun join(hallId: String) {
        viewModelScope.launch {
//            joinToGame(hallId).take(1).collect {
//                if (it != null) {
//                    _game.value = it
//                    printResumeGame()
//
//                    Log.d(TAG, "join: ")
//
//                    joinBoard(hallId)
//                    joinPlayer1(hallId)
//                    joinPlayer2(hallId)
//                    joinTurnPlayer(hallId)
//
//                    if (_uiState.value == GameViewState.LOADING) {
//                        _uiState.value = GameViewState.GAME
//                    }
//                }
//            }
        }
    }
    private fun joinBoard(hallId: String) {
        viewModelScope.launch {
//            joinToBoard(hallId).collect {
//                if (it != null) {
//                    Log.d(TAG, "joinBoard: $it")
//                    _game.value = _game.value?.copy(board = it.board)
//
//                    verifyWinner()
//
//                    clickResetPlayer(_game.value?.player1)
//                    clickResetPlayer(_game.value?.player2)
//
//
//                }
//            }
        }
    }

    private fun clickResetPlayer(player: PlayerModelUi?) {
        if (player?.resetGame == true && getPlayerType() == player.playerType) {
//            updatePlayer(_game.value?.hallId.orEmpty(),player.rstGame().toModelData())
            _uiState.value = GameViewState.GAME
        }
    }

    private fun joinPlayer1(hallId: String) {
        viewModelScope.launch {
//            joinToPlayer(hallId, PlayerType.FirstPlayer).collect {
//                if (it != null) {
//                    Log.d(TAG, "joinPlayer P1: $it")
//                    _game.value = _game.value?.copy(player1 = it)
//
//                    if (getPlayerType() != PlayerType.FirstPlayer) {
//                        setGameReady()
//                    } else {
//                        resetBoard()
//                    }
//                }
//            }
        }
    }
    private fun joinPlayer2(hallId: String) {
        viewModelScope.launch {
//            joinToPlayer(hallId, PlayerType.SecondPlayer).collect {
//                if (it != null) {
//                    Log.d(TAG, "joinPlayer P2: $it")
//                    _game.value = _game.value?.copy(player2 = it)
//
//                    if (getPlayerType() != PlayerType.SecondPlayer){
//                        setGameReady()
//                    } else {
//                        resetBoard()
//                    }
//                }
//            }
        }
    }
    private fun joinTurnPlayer(hallId: String) {
        viewModelScope.launch {
//            joinToPlayer(hallId, PlayerType.Empty).collect {
//                if (it != null) {
//
//                    Log.d(TAG, "joinPlayer Turn: $it")
//                    _game.value = _game.value?.copy(isMyTurn = isMyTurn(it))
//                }
//            }
        }
    }

    private fun resetBoard() {
        if (_game.value?.player1?.resetGame == true && _game.value?.player2?.resetGame == true) {
//            updateBoard(_game.value?.rstBoard()?.toModelData() ?: GameModelData())
        }
    }
    private fun setGameReady(){
        _game.value = _game.value?.copy(isGameReady = true)
    }
    private fun isMyTurn(playerTurn: PlayerModelUi) = playerTurn.userId == _user.value?.userId


    fun onClickItem(position: Int) {
        val gameAux = game.value ?: return
        if (!gameAux.isGameReady || !gameAux.isMyTurn || gameAux.board[position] != PlayerType.Empty) return

        val newBoard = gameAux.board.toMutableList()
        newBoard[position] = getPlayerType() ?: PlayerType.Empty

        viewModelScope.launch {
//            updateGame(
//                gameAux.copy(board = newBoard, playerTurn = getEnemyPlayer()!!).toModelData()
//            )
        }
    }
    private fun getEnemyPlayer(): PlayerModelUi? {
        return if (game.value?.player1?.userId == _user.value?.userId) game.value?.player2 else game.value?.player1
    }
    private fun getPlayerType(): PlayerType? {
        return when {
            game.value?.player1?.userId == _user.value?.userId -> PlayerType.FirstPlayer
            game.value?.player2?.userId == _user.value?.userId -> PlayerType.SecondPlayer
            else -> null
        }
    }


    private fun verifyWinner() {
        if (_uiState.value == GameViewState.GAME && _game.value?.isGameReady == true) {
            when {
                isSomebodyWinner(PlayerType.FirstPlayer) -> {
                    _uiState.value = GameViewState.FINISH
                    _winner.value = PlayerType.FirstPlayer

                    if (getPlayerType() == PlayerType.FirstPlayer) {
                        updateUserVictory(_user.value?.incVic()!!)
                        updatePlayerVictory(_game.value?.player1)
                    }
                    else {
                        updateUserVictory(_user.value?.incDef()!!)
                    }

                }

                isSomebodyWinner(PlayerType.SecondPlayer) -> {
                    _uiState.value = GameViewState.FINISH
                    _winner.value = PlayerType.SecondPlayer

                    if (getPlayerType() == PlayerType.SecondPlayer) {
                        updateUserVictory(_user.value?.incVic()!!)
                        updatePlayerVictory(_game.value?.player2)
                    } else {
                        updateUserVictory(_user.value?.incDef()!!)
                    }
                }

                isSomebodyWinner(PlayerType.Empty) -> {
                    _winner.value = PlayerType.Empty
                    _uiState.value = GameViewState.FINISH
                }
            }
        }
    }
    private fun isSomebodyWinner(playerType: PlayerType): Boolean {

        if (_game.value?.player1 == null) return false
        if (_game.value?.player2 == null) return false
        if (_game.value?.board == null) return false
        if (_game.value?.board!!.size != 9) return false

        val board = game.value!!.board

        if (playerType != PlayerType.Empty){
            return when {
                //Row
                (board[0] == playerType && board[1] == playerType && board[2] == playerType) -> true
                (board[3] == playerType && board[4] == playerType && board[5] == playerType) -> true
                (board[6] == playerType && board[7] == playerType && board[8] == playerType) -> true
                //Column
                (board[0] == playerType && board[3] == playerType && board[6] == playerType) -> true
                (board[1] == playerType && board[4] == playerType && board[7] == playerType) -> true
                (board[2] == playerType && board[5] == playerType && board[8] == playerType) -> true
                //Cross
                (board[0] == playerType && board[4] == playerType && board[8] == playerType) -> true
                (board[6] == playerType && board[4] == playerType && board[2] == playerType) -> true

                else -> false
            }
        } else {
            board.forEach {
                if(it == PlayerType.Empty) return false
            }
            return true
        }
    }

    private fun updateUserVictory(currentUser: UserModelUi) {
        viewModelScope.launch {
//            updateUser(currentUser.toModelData())
        }
    }
    private fun updatePlayerVictory(player: PlayerModelUi?) {
        val currentPlayer = player?.incVictory()

        viewModelScope.launch {
//            updatePlayer(_game.value?.hallId.orEmpty(), currentPlayer?.toModelData() ?: PlayerModelData())
        }
    }

    fun resetGame() {
        viewModelScope.launch {
//            if (getPlayerType() == PlayerType.FirstPlayer) {
//                updatePlayer(_game.value!!.hallId.orEmpty(),_game.value!!.player1?.rstGame(true)?.toModelData() ?: PlayerModelData())
//            } else {
//                updatePlayer(_game.value!!.hallId.orEmpty(),_game.value!!.player2?.rstGame(true)?.toModelData() ?: PlayerModelData())
//            }
        }
    }

    private fun printResumeGame() {

        Log.i("printResume", "{_uiState.value: ${_uiState.value}")
        Log.i("printResume", "{_winner.value: ${_winner.value}")
        Log.i("printResume", "{_victories.value: ${_victories.value}")
        Log.e("printResume", "{_game.value: ${_game.value}")
        Log.e("printResume", "{_game.value.hallId: ${_game.value!!.hallId}")
        Log.e("printResume", "{_game.value.hallName: ${_game.value!!.hallName}")
        Log.e("printResume", "{_game.value.board: ${_game.value!!.board}")
        Log.e("printResume", "{_game.value.player1: ${_game.value!!.player1}")
        Log.e("printResume", "{_game.value.player2: ${_game.value!!.player2}")
        Log.e("printResume", "{_game.value.playerTurn: ${_game.value!!.playerTurn}")
        Log.e("printResume", "{_game.value.isGameReady: ${_game.value!!.isGameReady}")
        Log.e("printResume", "{_game.value.isMyTurn: ${_game.value!!.isMyTurn}")
        Log.e(
            "printResume",
            "**************************************************************************"
        )
    }

    private fun printResumeUser() {
        Log.d("printResume", "{_user.value: ${_user.value}")
        Log.d("printResume", "{_user.value.userId: ${_user.value!!.userId}")
        Log.d("printResume", "{_user.value.nickname: ${_user.value!!.userName}")
        Log.d("printResume", "{_user.value.victories: ${_user.value!!.victories}")
        Log.d("printResume", "{_user.value.defeats: ${_user.value!!.defeats}")
        Log.d("printResume", "{_user.value.hallId: ${_user.value!!.lastHall}")
        Log.d(
            "printResume",
            "**************************************************************************"
        )
    }

    fun closeGame() {

    }


}

sealed class GameViewState {
    object LOADING : GameViewState()
    object GAME : GameViewState()
    object FINISH : GameViewState()
}

data class PlayerVictories(
    var player1: Int = 0,
    var player2: Int = 0
) {
    fun incP1(): PlayerVictories = this.copy(player1 = player1.inc())
    fun incP2(): PlayerVictories = this.copy(player2 = player2.inc())
}


