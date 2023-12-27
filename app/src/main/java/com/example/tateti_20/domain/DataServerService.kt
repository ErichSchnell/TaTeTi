package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.data.network.model.receive.GameModelDataReceive
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DataServerService {

    //USER
    suspend fun createUser(userId:String, userModelData: UserModelData): Boolean
    suspend fun getUser(userId: String): UserModelUi?
    suspend fun updateUser(userId: String, userData: UserModelData):Boolean


    suspend fun createHall(gameModelData: GameModelData): String



    fun joinToUser(userId: String)//: Flow<UserModelUi?>



    //GAME
    suspend fun joinToHall(gameId: String): Flow<GameModelData>//Flow<List<Int?>?>
//    suspend fun joinToHall(gameId: String)
    fun updateGame(gameModelData: GameModelData)

    fun joinToBoard(gameId: String)//: Flow<BoardModelUi?>
    fun updateBoard(gameModelData: GameModelData)

    fun joinToPlayer(gameId: String, player:PlayerType)//: Flow<PlayerModelUi?>
    fun updatePlayer(hallId: String, playerModelData: PlayerModelData)

    //HALLS
    fun getHalls()//: Flow<HallsModelUi?>



}