package com.example.tateti_20.ui.model

data class TrucoModelUI(
    val pointLimit:Int = 0,
    val player1Point:Int = 0,
    val player2Point:Int = 0,
    val winner: TypePlayer = TypePlayer.VACIO
) {

    fun toTrucoModelData() = TrucoModelUI()
}

sealed class TypePlayer(id:Int){
    object VACIO:TypePlayer(0)
    object PLAYER1:TypePlayer(1)
    object PLAYER2:TypePlayer(2)
}