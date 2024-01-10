package com.example.tateti_20.data.network

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.UserModelUi
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseService @Inject constructor(private val firestore: FirebaseFirestore) : DataServerService {

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
    override suspend fun getToHall(gameId: String): GameModelUi{
        return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_HALL).document(gameId).get()
                .addOnSuccessListener { document ->

                    val currentGame = getGameModelData(document)
                    Log.i("erich", "getToHall currentGame $currentGame ")

                    cancellableContinuation.resume(currentGame.toModelUi(gameId))

                }.addOnFailureListener { cancellableContinuation.resumeWithException(it) }
        }
    }
    override suspend fun joinToHall(gameId: String): Flow<GameModelUi> = callbackFlow {
        // Define el listener para los cambios en el documento

        val listener = EventListener<DocumentSnapshot> { snapshot, exception ->
            if (exception != null) {
                close(exception)
            } else if (snapshot != null && snapshot.exists()) {

                val currentGameData = getGameModelData(snapshot)

                Log.i("erich", "joinToHall snapshot: ${snapshot.data}")
                Log.i("erich", "joinToHall result: $currentGameData ")


                trySend(currentGameData.toModelUi(gameId))
            }
        }

        // Agrega el listener al documento
        val registration = firestore.collection(PATH_HALL).document(gameId).addSnapshotListener(listener)

        // Cierre el listener cuando se cancela el flujo
        awaitClose { registration.remove() }
    }
    override suspend fun updateToGame(hallId: String, gameModelData: GameModelData):Boolean {
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
            firestore.collection(PATH_HALL).document(hallId).set(hall).addOnSuccessListener {
                Log.i("erich", "firebase update ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }
    private fun getGameModelData(snapshot: DocumentSnapshot):GameModelData{

        val currentGame = snapshot.toObject(GameModelData::class.java) ?: GameModelData()
        Log.i("erich", "getGameModelData currentGame: $currentGame ")

        currentGame.hallName?.let {
            val result = currentGame.copy(
                isPublic = snapshot.getBoolean("isPublic"),
                isFinished = snapshot.getBoolean("isFinished"),
                isVisible = snapshot.getBoolean("isVisible"),
            )
            Log.i("erich", "getGameModelData result: $result ")
            return result
        }


        return currentGame
    }
    /*
                * --------------------------------------
                * */


    /*
    ------------------ HALLS ------------------
    */
    override fun getHalls(): Flow<List<GameModelUi>> = callbackFlow {
        // Define el listener para los cambios en el documento

        val listener = EventListener<QuerySnapshot> { collection, exception ->
            if (exception != null) {
                close(exception)
            } else if (collection != null) {

                val halls:MutableList<GameModelUi> = mutableListOf()
                collection.documents.forEach {
                    halls.add(it.toObject<GameModelData>()?.toModelUi(it.id) ?: GameModelData().toModelUi(""))
                }

                val currentGameData:List<GameModelUi> = halls

                Log.i("erich", "joinToHall snapshot: ${collection.documents}")
                Log.i("erich", "joinToHall result: $currentGameData ")


                trySend(currentGameData)
            }
        }

        // Agrega el listener al documento
        val registration = firestore.collection(PATH_HALL).whereEqualTo("isVisible" , true).addSnapshotListener(listener)

        // Cierre el listener cuando se cancela el flujo
        awaitClose { registration.remove() }
    }
    /*
                * --------------------------------------
                * */

}



/*val hallName_Aux   = snapshot.getString("hallName")
        val board_Aux     = snapshot.get("board") as List<Int?>?

        val player1_Aux     = snapshot.get("player1") as? Map<String, Any>
        val player1 = PlayerModelData(
            userId = player1_Aux?.get("user") as? String,
            playerType =  (player1_Aux?.get("playerType") as? Long)?.toInt(),
            victories = (player1_Aux?.get("victories") as? Long)?.toInt(),
            resetGame = player1_Aux?.get("resetGame") as? Boolean,
        )
        val player2_Aux     = snapshot.get("player2") as? Map<String, Any>
        val player2 = PlayerModelData(
            userId = player1_Aux?.get("user") as? String,
            playerType =  (player2_Aux?.get("playerType") as? Long)?.toInt(),
            victories = (player2_Aux?.get("victories") as? Long)?.toInt(),
            resetGame = player2_Aux?.get("resetGame") as? Boolean,
        )
        val playerTurn_Aux  = snapshot.get("playerTurn") as? Map<String, Any>
        val playerTurn = PlayerModelData(
            userId = playerTurn_Aux?.get("user") as? String,
            playerType =  (playerTurn_Aux?.get("playerType") as? Long)?.toInt(),
            victories = (playerTurn_Aux?.get("victories") as? Long)?.toInt(),
            resetGame = playerTurn_Aux?.get("resetGame") as? Boolean,
        )

        val isPublic_Aux    = snapshot.getBoolean("isPublic")
        val password_Aux    = snapshot.getString("password")

        val isFinished_Aux  = snapshot.getBoolean("isFinished")
        val isVisible_Aux   = snapshot.getBoolean("isVisible")
        val winner_Aux     = snapshot.getLong("winner")?.toInt()

        val currentGame = GameModelData(
            hallName =  hallName_Aux,
            board =  board_Aux,
            player1 =  player1,
            player2 =  player2,
            playerTurn =  playerTurn,
            isPublic =  isPublic_Aux,
            password =  password_Aux,
            isFinished =  isFinished_Aux,
            isVisible =  isVisible_Aux,
            winner =  winner_Aux
        )*/
