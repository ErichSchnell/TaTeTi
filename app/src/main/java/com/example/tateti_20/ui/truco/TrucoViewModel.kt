package com.example.tateti_20.ui.truco

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tateti_20.ui.model.TrucoModelUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun increasePlayer1() {
        _game.value = _game.value.copy(player1Point = _game.value.player1Point.inc())
        Log.i(TAG, "initAnnotator: ${_game.value}")
    }
    fun decreasePlayer1(){
        _game.value = _game.value.copy(player1Point = _game.value.player1Point.dec())
        Log.i(TAG, "initAnnotator: ${_game.value}")
    }
    fun increasePlayer2(){
        _game.value = _game.value.copy(player2Point = _game.value.player2Point.inc())
        Log.i(TAG, "initAnnotator: ${_game.value}")
    }
    fun decreasePlayer2(){
        _game.value = _game.value.copy(player2Point = _game.value.player2Point.dec())
        Log.i(TAG, "initAnnotator: ${_game.value}")
    }



}

sealed class TrucoState(){
    object LOADING:TrucoState()
    object SETTING:TrucoState()
    object READY:TrucoState()
}