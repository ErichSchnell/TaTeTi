package com.example.tateti_20.domain

import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.ui.model.AnnotatorHeadModelUi
import com.example.tateti_20.ui.model.GameModelUi
//import com.example.tateti_20.ui.model.HallsModelUi
import com.example.tateti_20.ui.model.UserModelUi
import kotlinx.coroutines.flow.Flow

interface DataServerService {

    /*
    ------------------ USER ------------------
    */
    suspend fun createUser(userId:String, userModelData: UserModelData): Boolean
    suspend fun updateUser(userId: String, userData: UserModelData):Boolean
    suspend fun getUser(userId: String): UserModelUi?
    /*
    ------------------------------------
    */


    /*
    ------------------ GAME ------------------
    */
    suspend fun createHall(gameModelData: GameModelData): String
    suspend fun getToHall(gameId: String): GameModelUi
    suspend fun joinToHall(gameId: String): Flow<GameModelUi>
    suspend fun updateToGame(hallId: String, gameModelData: GameModelData):Boolean
    /*
    ------------------------------------
    */

    /*
    ------------------ HALLS ------------------
    */
    fun getHalls(): Flow<List<GameModelUi>>
    /*
    ------------------------------------
    */

    /*
    ------------------ ANNOTATOR GAMES ------------------
    */
    fun getAnnotatorGames(userEmail:String): Flow<List<AnnotatorHeadModelUi>>
    /*
    ------------------------------------
    */



}