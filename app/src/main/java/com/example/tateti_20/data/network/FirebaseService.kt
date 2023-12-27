package com.example.tateti_20.data.network

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.data.network.model.receive.GameModelDataReceive
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.ui.model.PlayerType
import com.example.tateti_20.ui.model.UserModelUi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService @Inject constructor(private val firestore: FirebaseFirestore) :
    DataServerService {

    companion object {
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
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    override suspend fun getUser(userId: String): UserModelUi? {

        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).get()
                .addOnSuccessListener { document ->

                    val userModel = document.toObject<UserModelData>()
                    Log.i("erich", "firebase getUser $userModel ")

                    cancellableContinuation.resume(userModel?.toModelUi(userId))

                }.addOnFailureListener { cancellableContinuation.resumeWithException(it) }
        }
    }

    override suspend fun updateUser(userId: String, userData: UserModelData): Boolean {
        val user = hashMapOf(
            "userEmail" to userData.userEmail,
            "userName" to userData.userName,

            "victories" to userData.victories,
            "defeats" to userData.defeats,

            "lastHall" to userData.lastHall
        )

        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).set(user).addOnSuccessListener {
                Log.i("erich", "firebase update ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener {
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
        /*val player1 = hashMapOf(
            "user" to gameModelData.player1?.user?.let {
                firestore.collection(PATH_USER).document(it)
            },
            "playerType" to gameModelData.player1?.playerType,
            "victories" to gameModelData.player1?.victories,
            "resetGame" to gameModelData.player1?.resetGame
        )
        val player2 = hashMapOf(
            "user" to gameModelData.player2?.user?.let {
                firestore.collection(PATH_USER).document(it)
            },
            "playerType" to gameModelData.player2?.playerType,
            "victories" to gameModelData.player2?.victories,
            "resetGame" to gameModelData.player2?.resetGame
        )
        val playerTurn = hashMapOf(
            "user" to gameModelData.playerTurn?.user?.let {
                firestore.collection(PATH_USER).document(it)
            },
            "playerType" to gameModelData.playerTurn?.playerType,
            "victories" to gameModelData.playerTurn?.victories,
            "resetGame" to gameModelData.playerTurn?.resetGame
        )*/

        val hall = hashMapOf(
            "hallName" to gameModelData.hallName,

            "board" to gameModelData.board,

            "player1" to gameModelData.player1,
            "player2" to gameModelData.player2,
            "playerTurn" to gameModelData.playerTurn,

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
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    override suspend fun joinToHall(gameId: String) = callbackFlow {
        // Define el listener para los cambios en el documento

        val listener = EventListener<DocumentSnapshot> { snapshot, exception ->
            if (exception != null) {
                close(exception)
            } else if (snapshot != null && snapshot.exists()) {
                Log.i("erich", "joinToHall snapshot: ${snapshot.data}")

//                val result = snapshot.toObject(GameModelData::class.java) ?: GameModelData()
                val hallName_Aux   = snapshot.getString("hallName")
                val board_Aux     = snapshot.get("board") as List<Int?>?

                val player1_Aux     = snapshot.get("player1") as? Map<String, Any>
                val player1 = PlayerModelData(
                    user = player1_Aux?.get("user") as? String,
                    playerType =  (player1_Aux?.get("playerType") as? Long)?.toInt(),
                    victories = (player1_Aux?.get("victories") as? Long)?.toInt(),
                    resetGame = player1_Aux?.get("resetGame") as? Boolean,
                )
                val player2_Aux     = snapshot.get("player2") as? Map<String, Any>
                val player2 = PlayerModelData(
                    user = player2_Aux?.get("user") as? String,
                    playerType =  (player2_Aux?.get("playerType") as? Long)?.toInt(),
                    victories = (player2_Aux?.get("victories") as? Long)?.toInt(),
                    resetGame = player2_Aux?.get("resetGame") as? Boolean,
                )
                val playerTurn_Aux  = snapshot.get("playerTurn") as? Map<String, Any>
                val playerTurn = PlayerModelData(
                    user = playerTurn_Aux?.get("user") as? String,
                    playerType =  (playerTurn_Aux?.get("playerType") as? Long)?.toInt(),
                    victories = (playerTurn_Aux?.get("victories") as? Long)?.toInt(),
                    resetGame = playerTurn_Aux?.get("resetGame") as? Boolean,
                )

                val isPublic_Aux    = snapshot.getBoolean("isPublic")
                val password_Aux    = snapshot.getString("password")

                val isFinished_Aux  = snapshot.getBoolean("isFinished")
                val isVisible_Aux   = snapshot.getBoolean("isVisible")
                val winner_Aux     = snapshot.getLong("winner")?.toInt()

                val result = GameModelData(
                    hallName = hallName_Aux,
                    board = board_Aux,
                    player1 = player1,
                    player2 = player2,
                    playerTurn = playerTurn,
                    isPublic = isPublic_Aux,
                    password = password_Aux,
                    isFinished = isFinished_Aux,
                    isVisible = isVisible_Aux,
                    winner = winner_Aux,
                )
                Log.i("erich", "joinToHall hallName_Aux: $hallName_Aux ")
                Log.i("erich", "joinToHall board_Aux: $board_Aux ")
                Log.i("erich", "joinToHall player1_Aux: $player1_Aux ")
                Log.i("erich", "joinToHall player2_Aux: $player2_Aux ")
                Log.i("erich", "joinToHall playerTurn_Aux: $playerTurn_Aux ")
                Log.i("erich", "joinToHall isPublic_Aux: $isPublic_Aux ")
                Log.i("erich", "joinToHall password_Aux: $password_Aux ")
                Log.i("erich", "joinToHall isFinished_Aux: $isFinished_Aux ")
                Log.i("erich", "joinToHall isVisible_Aux: $isVisible_Aux ")
                Log.i("erich", "joinToHall winner_Aux: $0 ")
                Log.i("erich", "joinToHall result: $result ")

                trySend(result)
            }
        }

        // Agrega el listener al documento
        val registration = firestore.collection(PATH_HALL).document(gameId).addSnapshotListener(listener)

        // Cierre el listener cuando se cancela el flujo
        awaitClose { registration.remove() }
    }
    /*override suspend fun joinToHall(gameId: String){

        // Agrega el listener al documento
        val registration = firestore.collection(PATH_HALL).document(gameId).get().addOnSuccessListener{
            val result = it.toObject(GameModelData::class.java) ?: GameModelData()
            Log.i("erich", "joinToHall snapshot: ${it.data}")
            Log.i("erich", "joinToHall result: $result")
        }
    }*/

    /*override suspend fun joinToHall(gameId: String): Flow<GameModelDataReceive> {
        val _board = MutableStateFlow(GameModelDataReceive())
//        val board: StateFlow<GameModelDataReceive> = _board

        Log.i("erich", "gameId: $gameId")

        return suspendCancellableCoroutine { cancellableCoroutine ->
            firestore.collection(PATH_HALL).document(gameId).addSnapshotListener {value, error ->

                if(error != null) {
                    cancellableCoroutine.resumeWithException(error)
                }

                if (value != null) {
                    _board.value = value.toObject<GameModelDataReceive>() ?: GameModelDataReceive()
                    Log.i("erich", "joinToHall: ${_board.value}")
                    Log.i("erich", "joinToHall board: ${_board.value.board}")
                    Log.i("erich", "joinToHall hallName: ${_board.value.hallName}")
                    Log.i("erich", "joinToHall isFinished: ${_board.value.isFinished}")
                    Log.i("erich", "joinToHall password: ${_board.value.password}")
                    Log.i("erich", "joinToHall isVisible: ${_board.value.isVisible}")
                    Log.i("erich", "joinToHall player1: ${_board.value.player1}")
                    Log.i("erich", "joinToHall player2: ${_board.value.player2}")
                    Log.i("erich", "joinToHall playerTurn: ${_board.value.playerTurn}")
                    Log.i("erich", "joinToHall isPublic: ${_board.value.isPublic}")
                    cancellableCoroutine.resume(_board)
                }
            }
//            firestore.collection(PATH_HALL).document(gameId).get().addOnSuccessListener {
//            }
        }
    }*/

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