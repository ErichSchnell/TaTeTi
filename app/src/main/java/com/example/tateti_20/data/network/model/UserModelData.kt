package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.UserModelUi
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class UserModelData(
    val userEmail: String? = null,
    val userName: String? = null,

    val victories: Int? = null,
    val defeats: Int? = null,

    val lastHall: String? = null,
    val profilePhoto: Boolean? = null
) {
    fun toModelUi(userId: String): UserModelUi = UserModelUi(
        userId = userId,
        userEmail = userEmail.orEmpty(),
        userName = userName.orEmpty(),

        victories = victories ?: 0,
        defeats = defeats ?: 0,

        lastHall = lastHall.orEmpty(),
        profilePhoto = profilePhoto ?: false
    )
}
