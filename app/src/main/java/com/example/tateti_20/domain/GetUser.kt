package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.ui.model.UserModelUi
import com.example.tateti_20.ui.theme.string_log
import javax.inject.Inject

private const val TAG = string_log
class GetUser  @Inject constructor(private val dataServer: DataServerService) {
    suspend operator fun invoke(userId: String): UserModelUi {
        val userAux = dataServer.getUser(userId) ?: UserModelUi()
        Log.i(TAG, "invoke userAux: $userAux")

        return userAux
    }
}