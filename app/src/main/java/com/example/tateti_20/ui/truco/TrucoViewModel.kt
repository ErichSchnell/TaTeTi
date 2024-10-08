package com.example.tateti_20.ui.truco

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tateti_20.ui.model.TrucoModelUI
import com.example.tateti_20.ui.model.TypePlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "erich"
@HiltViewModel
class TrucoViewModel @Inject constructor(

): ViewModel() {

    private val _uiState = MutableStateFlow<TrucoState>(TrucoState.LOADING)
    val uiState:StateFlow<TrucoState> = _uiState

    private val _game = MutableStateFlow<TrucoModelUI>(TrucoModelUI())
    val game: StateFlow<TrucoModelUI> = _game

    fun initAnnotator(userEmail: String, annotatorTime: String) {
        _game.value = TrucoModelUI(pointLimit = 30)
        _uiState.value = TrucoState.READY

        Log.i(TAG, "initAnnotator: ${_game.value}")
    }



    fun setAnnotator(pointMax:Int){
        _game.value = _game.value.resetPoint(pointMax)
    }
    fun clearWinner(){
        _game.value = _game.value.clearWinner()
    }

    fun changeNamePlayer1(name: String) {
        _game.value = _game.value.setNamePlayer1(name)
        Log.i(TAG, "player1: ${_game.value.player1.playerName}")
    }
    fun increasePlayer1() {
        if(_game.value.player1.playerPoint == _game.value.pointLimit){
            _game.value = _game.value.copy(winner = TypePlayer.PLAYER1)

            Log.i(TAG, "player1 winner ?: ${_game.value.winner}")
        }
        if(_game.value.player1.playerPoint < _game.value.pointLimit){
            _game.value = _game.value.increasePlayer1()
        }

        Log.i(TAG, "player1: ${_game.value.player1.playerPoint}")
    }
    fun decreasePlayer1(){
        if(_game.value.player1.playerPoint > 0){
            _game.value = _game.value.decreasePlayer1()
        }
        Log.i(TAG, "player1: ${_game.value.player1.playerPoint}")
    }

    fun changeNamePlayer2(name: String) {
        _game.value = _game.value.setNamePlayer2(name)
        Log.i(TAG, "player2: ${_game.value.player2.playerName}")
    }
    fun increasePlayer2(){
        if(_game.value.player2.playerPoint == _game.value.pointLimit){
            _game.value = _game.value.copy(winner = TypePlayer.PLAYER2)
            Log.i(TAG, "player2 winner ?: ${_game.value.winner}")
        }
        if(_game.value.player2.playerPoint < _game.value.pointLimit){
            _game.value = _game.value.increasePlayer2()
        }
        Log.i(TAG, "player2: ${_game.value.player2.playerPoint}")
    }
    fun decreasePlayer2(){
        if(_game.value.player2.playerPoint > 0){
            _game.value = _game.value.decreasePlayer2()
        }
        Log.i(TAG, "player2: ${_game.value.player2.playerPoint}")
    }


}

sealed class TrucoState(){
    object LOADING:TrucoState()
    object SETTING:TrucoState()
    object READY:TrucoState()
}