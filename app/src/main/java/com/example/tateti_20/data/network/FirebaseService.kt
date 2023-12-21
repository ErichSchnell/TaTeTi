package com.example.tateti_20.data.network

import android.util.Log
import com.example.tateti_20.data.network.model.BoardModelData
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.HallsModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.ui.model.BoardModelUi
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.GameModelUiNames.*
import com.example.tateti_20.ui.model.HallsModelUi
import com.example.tateti_20.ui.model.PlayerModelUi
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService @Inject constructor(private val firestore: FirebaseFirestore): DataServerService {

    companion object{
        private const val PATH_HALL = "halls"
        private const val PATH_USER = "users"
    }

    override suspend fun createUser(userModelData: UserModelData): Boolean {
        val user = hashMapOf(
            "userId" to userModelData.userId,
            "userEmail" to userModelData.userEmail,
            "userName" to userModelData.userName,
            "victories" to userModelData.victories,
            "defeats" to userModelData.defeats,
            "lastHall" to userModelData.lastHall
        )

        val result = firestore.collection("users").add(user).addOnSuccessListener {
            Log.i("createUser", "createUser: true")
        }.addOnFailureListener{
            Log.i("createUser", "createUser: false")
        }.await()

        return result != null
    }

    override fun joinToUser(userId: String) {
    }

    override fun updateUser(userData: UserModelData) {
    }

    override fun createGame(gameModelData: GameModelData): String {
        return ""
    }

    override fun joinToGame(gameId: String) {
    }

    override fun updateGame(gameModelData: GameModelData) {
    }

    override fun joinToBoard(gameId: String) {
    }

    override fun updateBoard(gameModelData: GameModelData) {
    }

    override fun joinToPlayer(gameId: String, player: PlayerType) {
    }

    override fun updatePlayer(hallId: String, playerModelData: PlayerModelData) {
    }

    override fun getHalls() {//: Flow<HallsModelUi?>
    }

//    //------------ USER -------------
//    override fun createUser(userModelData: UserModelData): String {
////        val userReference = reference.child(PATH_USER).push()
////        val newUser = userModelData.copy(userId = userReference.key)
////        userReference.setValue(newUser)
////        return userReference.key.orEmpty()
//    }
//    override fun joinToUser(userId: String){}//: Flow<UserModelUi?> {
////        return reference.database.reference.child("$PATH_USER/$userId").snapshots.map { dataSnapshot ->
////            dataSnapshot.getValue(UserModelData::class.java)?.toModelUi()
////        }
//    }
//    override fun updateUser(userModelData: UserModelData) {
////        reference.child("$PATH_USER/${userModelData.userId}").setValue(userModelData)
//    }
//
//    //------------ HALLS -------------
//    override fun getHalls(){}//}: Flow<HallsModelUi?> {
////        return reference.database.reference.child(PATH_GAME).snapshots.map { dataSnapshot ->
////            Log.d("erich", "getHalls: antes del val ")
//////            val values= dataSnapshot.getValue(object : GenericTypeIndicator<List<GameModelData?>?>() {})
////            val dataMap = dataSnapshot.getValue(object : GenericTypeIndicator<Map<String, GameModelData?>?>() {})
////
////            // Extrae la lista de valores del mapa
////            val values = dataMap?.values?.toList()
////            Log.d("erich", "getHalls: despues del val ")
////            HallsModelData(values).toModelUi()
////        }
////    }
//
//    //------------ GAME -------------
//    override fun createGame(gameModelData: GameModelData): String {
////        val gameReference = reference.child(PATH_GAME).push()
////        val newGame = gameModelData.copy(hallId = gameReference.key)
////        Log.d("printResume", "createGame newGame: $newGame")
////        gameReference.setValue(newGame)
//        return ""//gameReference.key.orEmpty()
//    }
//    override fun joinToGame(gameId: String){}//}: Flow<GameModelUi?> {
////        return reference.database.reference.child("$PATH_GAME/$gameId").snapshots.map { dataSnapshot ->
////            dataSnapshot.getValue(GameModelData::class.java)?.toModelUi()
////        }
////    }
//    override fun updateGame(gameModelData: GameModelData) {
////        reference.child("$PATH_GAME/${gameModelData.hallId}").setValue(gameModelData)
//    }
//
//
//    //------------ BOARD -------------
//    override fun joinToBoard(gameId: String){}//}: Flow<BoardModelUi?> {
////        return reference.database.reference.child("$PATH_GAME/$gameId/${BOARD.valueName}").snapshots.map { dataSnapshot ->
////            val values: List<Int?>? = dataSnapshot.getValue(object : GenericTypeIndicator<List<Int?>?>() {})
////            BoardModelData(values).toModelUi()
////        }
////    }
//    override fun updateBoard(gameModelData: GameModelData) {
////        reference.child("$PATH_GAME/${gameModelData.hallId}/${BOARD.valueName}").setValue(gameModelData.board)
//    }
//
//
//    //------------ PLAYER -------------
//    override fun joinToPlayer(hallId: String, player: PlayerType) {//: Flow<PlayerModelUi?>
//        val rootPlayer = when (player){
//            PlayerType.FirstPlayer -> PLAYER_1.valueName
//            PlayerType.SecondPlayer -> PLAYER_2.valueName
//            PlayerType.Empty -> PLAYER_TURN.valueName
//        }
////
////        return reference.database.reference.child("$PATH_GAME/$hallId/$rootPlayer").snapshots.map { dataSnapshot ->
////            dataSnapshot.getValue(PlayerModelData::class.java)?.toModelUi()
////        }
//    }
//    override fun updatePlayer(hallId: String, playerModelData: PlayerModelData) {
//        val rootPlayer = when (playerModelData.playerType){
//            2 -> PLAYER_1.valueName
//            3 -> PLAYER_2.valueName
//            0 -> PLAYER_TURN.valueName
//            else -> {PLAYER_TURN.valueName}
//        }
//
////        reference.child("$PATH_GAME/$hallId/$rootPlayer").setValue(playerModelData)
//    }
}