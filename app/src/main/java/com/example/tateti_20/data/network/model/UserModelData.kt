package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.UserModelUi


data class UserModelData(
    val userId: String? = null,
    val userEmail:String? = null,
    val userName:String? = null,

    val victories: Int? = null,
    val defeats: Int? = null,

    val lastHall: String? = null,
) {
    fun toModelUi(): UserModelUi = UserModelUi(
        userId = userId.orEmpty() ,
        userName = userName.orEmpty(),
        victories = victories ?: 0,
        defeats = defeats ?: 0,
        lastHall = lastHall.orEmpty(),
    )
}
