package com.example.tateti_20.domain

import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.UserModelUi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class JoinToUser @Inject constructor(private val dataServerService: DataServerService) {
    operator fun invoke(userId:String){}
//    : Flow<UserModelUi?> {
//        return dataServerService.joinToUser(userId)
//    }
}