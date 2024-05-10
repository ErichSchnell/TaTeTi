package com.example.tateti_20.ui.model

data class TrucoModelUI(
    val pointLimit:Int = 0,
    val player1: TrucoPlayerModelUi = TrucoPlayerModelUi("Nosotros"),
    val player2: TrucoPlayerModelUi = TrucoPlayerModelUi("Ellos"),
    val winner: TypePlayer = TypePlayer.VACIO
) {

    fun toTrucoModelData() = TrucoModelUI()

    fun setNamePlayer1(name:String) = this.copy(player1 = this.player1.setName(name))
    fun increasePlayer1() = this.copy(player1 = this.player1.increasePoint())
    fun decreasePlayer1() = this.copy(player1 = this.player1.decreasePoint())
    fun setNamePlayer2(name:String) = this.copy(player2 = this.player2.setName(name))
    fun increasePlayer2() = this.copy(player2 = this.player2.increasePoint())
    fun decreasePlayer2() = this.copy(player2 = this.player2.decreasePoint())

}

data class TrucoPlayerModelUi(
    val playerName:String = "",
    val playerPoint:Int = 0,
) {
    fun setName(name:String) = this.copy(playerName = name)
    fun increasePoint() = this.copy(playerPoint = playerPoint.inc())
    fun decreasePoint() = this.copy(playerPoint = playerPoint.dec())
}

sealed class TypePlayer(id:Int){
    object VACIO:TypePlayer(0)
    object PLAYER1:TypePlayer(1)
    object PLAYER2:TypePlayer(2)
}