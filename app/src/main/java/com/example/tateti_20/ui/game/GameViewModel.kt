package com.example.tateti_20.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.domain.GetToHall
import com.example.tateti_20.domain.GetUser
import com.example.tateti_20.domain.JoinToHall
import com.example.tateti_20.domain.UpdateGame
import com.example.tateti_20.domain.UpdateUser
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private val TAG = "Erich"

@HiltViewModel
class GameViewModel @Inject constructor(
    private val joinToHall: JoinToHall,
    private val getHall: GetToHall,
    private val updateGame: UpdateGame,
//
//    private val joinToBoard: JoinToBoard,
//    private val updateBoard: UpdateBoard,
//
//    private val joinToPlayer: JoinToPlayer,
//    private val updatePlayer: UpdatePlayer,
//
    private val getUser: GetUser,
    private val setUser: UpdateUser
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameViewState>(GameViewState.LOADING)
    val uiState: StateFlow<GameViewState> = _uiState

    private val _game = MutableStateFlow<GameModelUi?>(null)
    val game: StateFlow<GameModelUi?> = _game

    private val _winner = MutableStateFlow<PlayerType>(PlayerType.Empty)
    val winner: StateFlow<PlayerType> = _winner

    private val _myUser = MutableStateFlow(UserModelUi())

    private val _player1 = MutableStateFlow<UserModelUi?>(null)
    val player1: StateFlow<UserModelUi?> = _player1

    private val _player2 = MutableStateFlow<UserModelUi?>(null)
    val player2: StateFlow<UserModelUi?> = _player2

    private val _victories = MutableStateFlow<PlayerVictories>(PlayerVictories())
    val victories: StateFlow<PlayerVictories> = _victories


    /*
    *   USER
    */
    fun initGame(userId: String, hallId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("erich", "initGame: (userId: $userId \thallId: $hallId)")
            try {
                _myUser.value = async{ getUser(userId) }.await()

                if(_myUser.value.userEmail.isNotEmpty()){
                    Log.i(TAG, "initGame _myUser.value: ${_myUser.value}")

                    getGame(hallId)
                }

            } catch (e: Exception) {
                Log.i(TAG, "initGame error: ${e.message}")
            }
        }
    }

    /*
    *   GAME
    */
    fun getGame(hallId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async{ getHall(hallId) }.await()

            if (result.hallName.isNotEmpty()){

                Log.i(TAG, "getGame result: ${result}")

                if(_myUser.value.userId == result.player1?.userId)  {
                    _player1.value = _myUser.value
                }
                if(_myUser.value.userId != result.player1?.userId &&  result.player2 == null){
                    _player2.value = _myUser.value
                    _game.value = result.copy(player2 = _myUser.value.toPlayer(PlayerType.SecondPlayer))
                    updateGame(result.hallId.orEmpty() ,_game.value?.toModelData() ?: GameModelData())
                }

                _uiState.value = GameViewState.GAME

                join(hallId)
            }
        }
    }

    private fun join(hallId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            joinToHall(hallId).collect{currentGame ->
                currentGame.hallId?.let {


                    Log.i(TAG, "join: update")
                    _game.value = currentGame.copy(
                        isGameReady = (currentGame.player2 != null),
                        isMyTurn = isMyTurn(currentGame.playerTurn)
                    )

                    verifyWinner()
                    resetBoard()

                    if(_player1.value == null && currentGame.player1 != null){
                        _player1.value = async { getUser(currentGame.player1.userId)}.await()
                    }
                    if(_player2.value == null && currentGame.player2 != null){
                        _player2.value = async { getUser(currentGame.player2.userId)}.await()
                    }
                }
            }
        }
    }

    private fun resetBoard() {
        viewModelScope.launch(Dispatchers.IO){
            if (_game.value?.player1?.resetGame == true && _game.value?.player2?.resetGame == true) {
                val player1 = _game.value?.player1?.copy(resetGame = false)
                val player2 = _game.value?.player2?.copy(resetGame = false)
                updateGame(
                    hallId = _game.value?.hallId.orEmpty(),
                    gameModelData = _game.value?.copy(player1 = player1, player2 = player2)?.toModelData() ?: GameModelData()
                )
            }
        }
    }
    private fun setGameReady(){
        _game.value = _game.value?.copy(isGameReady = true)
    }
    private fun isMyTurn(playerTurn: PlayerModelUi?) = playerTurn?.userId == _myUser.value.userId


    fun onClickItem(position: Int) {
        val gameAux = game.value ?: return
        if (!gameAux.isGameReady || !gameAux.isMyTurn || gameAux.board[position] != PlayerType.Empty) return

        val newBoard = gameAux.board.toMutableList()
        newBoard[position] = getPlayerType() ?: PlayerType.Empty

        viewModelScope.launch {

            updateGame(
                hallId = gameAux.hallId.orEmpty(),
                gameModelData = gameAux.copy(board = newBoard, playerTurn = getEnemyPlayer()!!).toModelData()
            )
        }
    }
    private fun getEnemyPlayer(): PlayerModelUi? {
        return if (game.value?.player1?.userId == _myUser.value.userId) game.value?.player2 else game.value?.player1
    }
    private fun getPlayerType(): PlayerType? {
        return when {
            game.value?.player1?.userId == _myUser.value.userId -> PlayerType.FirstPlayer
            game.value?.player2?.userId == _myUser.value.userId -> PlayerType.SecondPlayer
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
                        updateUserVictory(_myUser.value.incVic())
                        updatePlayerVictory(_game.value?.player1)
                    }
                    else {
                        updateUserVictory(_myUser.value.incDef())
                    }

                }

                isSomebodyWinner(PlayerType.SecondPlayer) -> {
                    _uiState.value = GameViewState.FINISH
                    _winner.value = PlayerType.SecondPlayer

                    if (getPlayerType() == PlayerType.SecondPlayer) {
                        updateUserVictory(_myUser.value.incVic())
                        updatePlayerVictory(_game.value?.player2)
                    } else {
                        updateUserVictory(_myUser.value.incDef())
                    }
                }

                isSomebodyWinner(PlayerType.Empty) -> {
                    _winner.value = PlayerType.Empty
                    _uiState.value = GameViewState.FINISH

                    viewModelScope.launch(Dispatchers.IO){
                        updateGame(
                            hallId = _game.value?.hallId.orEmpty(),
                            gameModelData = _game.value?.rstBoard()?.toModelData() ?: GameModelData()
                        )
                    }
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
            setUser(currentUser)
        }
    }
    private fun updatePlayerVictory(player: PlayerModelUi?) {
        val currentPlayer = player?.incVictory()

        viewModelScope.launch {
            if (player?.playerType == PlayerType.FirstPlayer){
                updateGame(
                    hallId = _game.value?.hallId.orEmpty(),
                    gameModelData = _game.value?.rstBoard(true)?.copy(player1 = currentPlayer)?.toModelData() ?: GameModelData()//currentPlayer?.toModelData() ?: PlayerModelData()
                )
            } else{
                updateGame(
                    hallId = _game.value?.hallId.orEmpty(),
                    gameModelData = _game.value?.rstBoard(true)?.copy(player2 = currentPlayer)?.toModelData() ?: GameModelData()//currentPlayer?.toModelData() ?: PlayerModelData()
                )
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            if (getPlayerType() == PlayerType.FirstPlayer) {
                val player = _game.value!!.player1?.rstGame(true)
                updateGame(_game.value!!.hallId.orEmpty(), _game.value?.copy(player1 = player)?.toModelData() ?: GameModelData())
            } else {
                val player = _game.value!!.player2?.rstGame(true)
                updateGame(_game.value!!.hallId.orEmpty(), _game.value?.copy(player2 = player)?.toModelData() ?: GameModelData())
            }
            _uiState.value = GameViewState.GAME
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
        Log.d("printResume", "{_user.value: ${_myUser.value}")
        Log.d("printResume", "{_user.value.userId: ${_myUser.value!!.userId}")
        Log.d("printResume", "{_user.value.nickname: ${_myUser.value!!.userName}")
        Log.d("printResume", "{_user.value.victories: ${_myUser.value!!.victories}")
        Log.d("printResume", "{_user.value.defeats: ${_myUser.value!!.defeats}")
        Log.d("printResume", "{_user.value.hallId: ${_myUser.value!!.lastHall}")
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


