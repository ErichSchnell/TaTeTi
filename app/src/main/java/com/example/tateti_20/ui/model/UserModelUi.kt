package com.example.tateti_20.ui.model

import com.example.tateti_20.data.network.model.UserModelData

data class UserModelUi(
    val userId: String = "",
    val userEmail:String = "",
    val userName:String = "",

    val victories: Int = 0,
    val defeats: Int = 0,

    val lastHall: String = "",
) {
    fun toModelData(): UserModelData = UserModelData(
        userEmail = userEmail,
        userName = userName,

        victories = victories,
        defeats = defeats,

        lastHall = lastHall,
    )
    fun toPlayer(playerType: PlayerType): PlayerModelUi = PlayerModelUi(
        user = this,
        playerType = playerType
    )

    fun incVic():UserModelUi{
        return this.copy(
            victories = victories.inc()
        )
    }
    fun incDef():UserModelUi{
        return this.copy(
            defeats = defeats.inc()
        )
    }
}