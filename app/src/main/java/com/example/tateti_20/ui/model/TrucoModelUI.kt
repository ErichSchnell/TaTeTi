package com.example.tateti_20.ui.model

data class TrucoModelUI(
    val pointLimit:Int = 0,
    val player1: TrucoPlayerModelUi = TrucoPlayerModelUi("Nosotros"),
    val player2: TrucoPlayerModelUi = TrucoPlayerModelUi("Ellos"),
    val winner: TypePlayer = TypePlayer.VACIO
) {

    fun toTrucoModelData() = TrucoModelUI()

    fun resetPoint(point:Int) = this.copy(
        pointLimit = point,
        player1 = this.player1.setPoint(0),
        player2 = this.player2.setPoint(0),
        winner = TypePlayer.VACIO
    )
    fun clearWinner() = this.copy(
        winner = TypePlayer.VACIO
    )
    fun setNamePlayer1(name:String) = this.copy(player1 = this.player1.setName(name))
    fun increasePlayer1() = this.copy(player1 = this.player1.setPoint(this.player1.playerPoint.inc()))
    fun decreasePlayer1() = this.copy(player1 = this.player1.setPoint(this.player1.playerPoint.dec()))
    fun setNamePlayer2(name:String) = this.copy(player2 = this.player2.setName(name))
    fun increasePlayer2() = this.copy(player2 = this.player2.setPoint(this.player2.playerPoint.inc()))
    fun decreasePlayer2() = this.copy(player2 = this.player2.setPoint(this.player2.playerPoint.dec()))

}

data class TrucoPlayerModelUi(
    val playerName:String = "",
    val playerPoint:Int = 0,
) {
    fun setName(name:String) = this.copy(playerName = name)
    fun setPoint(point:Int) = this.copy(playerPoint = point)
}

sealed class TypePlayer(id:Int){
    object VACIO:TypePlayer(0)
    object PLAYER1:TypePlayer(1)
    object PLAYER2:TypePlayer(2)
}