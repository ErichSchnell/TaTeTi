package com.example.tateti_20.data.network

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService @Inject constructor(private val firestore: FirebaseFirestore): DataServerService {

    companion object{
        private const val PATH_HALL = "halls"
        private const val PATH_USER = "users"
    }


/*
    * ------------- USERS -----------------
    * */
    override suspend fun createUser(userId: String, userModelData: UserModelData): Boolean {
        val user = hashMapOf(
            "userEmail" to userModelData.userEmail,
            "userName" to userModelData.userName,

            "victories" to userModelData.victories,
            "defeats" to userModelData.defeats,

            "lastHall" to userModelData.lastHall
        )

        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).set(user).addOnSuccessListener {
                Log.i("erich", "firebase createUser ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener{
                cancellableContinuation.resumeWithException(it)
            }
        }
    }
    override suspend fun getUser(userId: String): UserModelUi? {

        return suspendCancellableCoroutine {cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).get().addOnSuccessListener{ document ->

                val userModel = document.toObject<UserModelData>()
                Log.i("erich", "firebase getUser $userModel ")

                cancellableContinuation.resume(userModel?.toModelUi(userId))

            }.addOnFailureListener { cancellableContinuation.resumeWithException(it) }
        }
    }
    override suspend fun updateUser(userId: String, userModelData: UserModelData):Boolean {
        val user = hashMapOf(
            "userEmail" to userModelData.userEmail,
            "userName" to userModelData.userName,

            "victories" to userModelData.victories,
            "defeats" to userModelData.defeats,

            "lastHall" to userModelData.lastHall
        )

        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).set(user).addOnSuccessListener {
                Log.i("erich", "firebase update ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener{
                cancellableContinuation.resumeWithException(it)
            }
        }
    }
    override fun joinToUser(userId: String) {
    }
/*
        * --------------------------------------
        * */




/*
    * ------------- GAME -----------------
    * */
    override suspend fun createHall(gameModelData: GameModelData): String {
    val player1 = hashMapOf(
        "user" to gameModelData.player1?.user?.let { firestore.collection(PATH_USER).document(it) },
        "playerType" to gameModelData.player1?.playerType,
        "victories" to gameModelData.player1?.victories,
        "resetGame" to gameModelData.player1?.resetGame
    )
    val player2 = hashMapOf(
        "user" to gameModelData.player2?.user?.let { firestore.collection(PATH_USER).document(it) },
        "playerType" to gameModelData.player2?.playerType,
        "victories" to gameModelData.player2?.victories,
        "resetGame" to gameModelData.player2?.resetGame
    )
    val playerTurn = hashMapOf(
        "user" to gameModelData.playerTurn?.user?.let { firestore.collection(PATH_USER).document(it) },
        "playerType" to gameModelData.playerTurn?.playerType,
        "victories" to gameModelData.playerTurn?.victories,
        "resetGame" to gameModelData.playerTurn?.resetGame
    )

        val hall = hashMapOf(
            "hallName" to gameModelData.hallName,

            "board" to gameModelData.board,

            "player1" to player1,
            "player2" to player2,
            "playerTurn" to playerTurn,

            "isPublic" to gameModelData.isPublic,
            "password" to gameModelData.password,

            "isFinished" to gameModelData.isFinished,
            "isVisible" to gameModelData.isVisible,
            "winner" to gameModelData.winner
        )

        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_HALL).add(hall).addOnSuccessListener {
                Log.i("erich", "firebase createHall ")
                cancellableContinuation.resume(it.id)
            }.addOnFailureListener{
                cancellableContinuation.resumeWithException(it)
            }
        }
    }
    override fun joinToHall(gameId: String) {

        Log.i("erich", "gameId: $gameId")

        firestore.collection(PATH_HALL).document(gameId).addSnapshotListener { value, error ->
            value?.let {
                Log.i("erich", "value: $value")
            }
        }
    }
    override fun joinToBoard(gameId: String) {
    }
    override fun joinToPlayer(gameId: String, player: PlayerType) {
    }
/*
            * --------------------------------------
            * */






    override fun updateGame(gameModelData: GameModelData) {
    }

    override fun updateBoard(gameModelData: GameModelData) {
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