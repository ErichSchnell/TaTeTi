package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.UserModelUi


data class UserModelData(
    val userEmail:String? = null,
    val userName:String? = null,

    val victories: Int? = null,
    val defeats: Int? = null,

    val lastHall: String? = null,
    val profilePhoto: Boolean? = null,
) {
    fun toModelUi(userId:String): UserModelUi = UserModelUi(
        userId = userId,
        userName = userName.orEmpty(),
        userEmail = userEmail.orEmpty(),

        victories = victories ?: 0,
        defeats = defeats ?: 0,
        lastHall = lastHall.orEmpty(),
        profilePhoto = profilePhoto ?: false
    )
}
