package com.example.tateti_20.domain

import android.util.Log
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.theme.string_log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = string_log
class JoinToHall @Inject constructor(private val dataServerService: DataServerService) {
    suspend operator fun invoke(gameId: String) : Flow<GameModelUi>{
        Log.i(TAG, "invoke gameId: $gameId")

        return dataServerService.joinToHall(gameId)
    }
}