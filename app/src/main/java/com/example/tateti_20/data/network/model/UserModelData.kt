package com.example.tateti_20.data.network.model

import com.example.tateti_20.ui.model.UserModelUi
import com.google.errorprone.annotations.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class UserModelData(
    @get:PropertyName("userEmail") @PropertyName("userEmail") val userEmail: String? = null,
    @get:PropertyName("userName") @PropertyName("userName") val userName: String? = null,

    @get:PropertyName("victories") @PropertyName("victories") val victories: Int? = null,
    @get:PropertyName("defeats") @PropertyName("defeats") val defeats: Int? = null,

    @get:PropertyName("lastHall") @PropertyName("lastHall") val lastHall: String? = null,
    @get:PropertyName("profilePhoto") @PropertyName("profilePhoto") val profilePhoto: Boolean? = null
) {
    constructor(): this(null,null,null,null,null,null)
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
