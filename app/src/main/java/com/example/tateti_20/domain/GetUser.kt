package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.ui.model.UserModelUi
import javax.inject.Inject

class GetUser  @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(userId: String): UserModelUi {
        val userAux = dataServer.getUser(userId) ?: UserModelUi()
        Log.i("erich", "invoke userAux: $userAux")

        return userAux
    }
}