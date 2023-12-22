package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi

interface DataServerService {

    //USER
    suspend fun createUser(userModelUi: UserModelUi): Boolean
    suspend fun getUser(userId: String): UserModelUi?
    fun joinToUser(userId: String)//: Flow<UserModelUi?>
    fun updateUser(userData: UserModelData)



    //GAME
    fun createGame(gameModelData: GameModelData): String
    fun joinToGame(gameId: String)//: Flow<GameModelUi?>
    fun updateGame(gameModelData: GameModelData)

    fun joinToBoard(gameId: String)//: Flow<BoardModelUi?>
    fun updateBoard(gameModelData: GameModelData)

    fun joinToPlayer(gameId: String, player:PlayerType)//: Flow<PlayerModelUi?>
    fun updatePlayer(hallId: String, playerModelData: PlayerModelData)

    //HALLS
    fun getHalls()//: Flow<HallsModelUi?>


}