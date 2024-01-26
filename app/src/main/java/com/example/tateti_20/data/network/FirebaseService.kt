package com.example.tateti_20.data.network

import android.util.Log
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.ui.model.GameModelUi
import com.example.tateti_20.ui.model.UserModelUi
import com.example.tateti_20.ui.theme.string_log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = string_log

class FirebaseService @Inject constructor(private val firestore: FirebaseFirestore) : DataServerService {

    companion object {
        private const val PATH_HALL = "halls"
        private const val PATH_USER = "users"
    }


    /*
        * ------------- USERS -----------------
        * */
    override suspend fun createUser(userId: String, userModelData: UserModelData): Boolean {
        try {
            firestore.collection(PATH_USER).document(userId).set(userModelData).await()
            return true
        } catch (e:Exception){
            Log.e(TAG, "createUser: ${e.message}", e)
            throw e
        }
        /*return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).set(userModelData).addOnSuccessListener {
                Log.i(TAG, "firebase createUser ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }*/
    }
    override suspend fun updateUser(userId: String, userData: UserModelData): Boolean {
        try {
            firestore.collection(PATH_USER).document(userId).set(userData).await()
            return true
        } catch (e:Exception){
            Log.e(TAG, "updateUser: ${e.message}", e)
            throw e
        }
        /*return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_USER).document(userId).set(userData).addOnSuccessListener {
                Log.i(TAG, "firebase update ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }*/
    }
    override suspend fun getUser(userId: String): UserModelUi? {
        try {
            val snapshot = firestore.collection(PATH_USER).document(userId).get().await()
            Log.d(TAG, "getUser snapshot: ${snapshot.data.toString()}")
            if (snapshot.exists()){

//                val currentUser = snapshot.toObject<UserModelData>()
                val currentUser = snapshot.toObject<UserModelData>()
                Log.d(TAG, "currentUser: $currentUser")
                val currentUserUi = currentUser?.toModelUi(userId)
                Log.d(TAG, "currentUserUi: $currentUserUi")

                return currentUserUi
            }
        } catch (e:Exception){
            Log.e(TAG, "getUser: ${e.message}", e)
            throw e
        }
        return null
    }
//    private fun getUserModelData(snapshot: DocumentSnapshot): UserModelData {
//
//        Log.i(TAG, "getUserModelData snapshot: ${snapshot.data}")
//
//        val userEmailAux = snapshot.data?.get("userEmail") as? String
//        val userNameAux = snapshot.data?.get("userName") as? String
//        val victoriesAux = (snapshot.data?.get("victories") as? Long)?.toInt()
//        val defeatsAux = (snapshot.data?.get("defeats") as? Long)?.toInt()
//        val lastHallAux = snapshot.data?.get("lastHall") as? String
//        val profilePhotoAux = snapshot.data?.get("profilePhoto") as? Boolean
//
//        val result = UserModelData(
//            userEmail =  userEmailAux.orEmpty(),
//            userName =  userNameAux,
//            victories =  victoriesAux,
//            defeats =  defeatsAux,
//            lastHall =  lastHallAux,
//            profilePhoto =  profilePhotoAux
//        )
//
//        Log.d(TAG, "---------------------- getUserModelData ----------------------")
//        Log.d(TAG, "getUserModelData userEmail: $userEmailAux ")
//        Log.d(TAG, "getUserModelData userName: $userNameAux ")
//        Log.d(TAG, "getUserModelData victories: $victoriesAux ")
//        Log.d(TAG, "getUserModelData defeats: $defeatsAux ")
//        Log.d(TAG, "getUserModelData lastHall: $lastHallAux ")
//        Log.d(TAG, "getUserModelData profilePhoto: $profilePhotoAux ")
//        Log.d(TAG, "getUserModelData result: $result ")
//        Log.d(TAG, "--------------------------------------------------------------")
//
//        return result
//    }
    /*
            * --------------------------------------
            * */


    /*
        * ------------- GAME -----------------
        * */
    override suspend fun createHall(gameModelData: GameModelData): String {
        try {
            val reference = firestore.collection(PATH_HALL).add(gameModelData).await()
            return reference.id
        } catch (e:Exception) {
            Log.e(TAG, "createHall: ${e.message}", e)
            throw e
        }
        /*return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_HALL).add(gameModelData).addOnSuccessListener {
                Log.i(TAG, "firebase createHall ")
                cancellableContinuation.resume(it.id)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }*/
    }
    override suspend fun updateToGame(hallId: String, gameModelData: GameModelData):Boolean {
        try {
            firestore.collection(PATH_HALL).document(hallId).set(gameModelData).await()
            return true
        } catch (e:Exception){
            Log.e(TAG, "updateToGame: ${e.message}", e)
            throw e
        }
        /*return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_HALL).document(hallId).set(gameModelData).addOnSuccessListener {
                Log.i(TAG, "firebase update ")
                cancellableContinuation.resume(true)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }*/
    }
    override suspend fun getToHall(gameId: String): GameModelUi{
        try {
            val snapshot = firestore.collection(PATH_HALL).document(gameId).get().await()
            if (snapshot.exists()){
                return snapshot.toObject<GameModelData>()?.toModelUi(gameId) ?: GameModelData().toModelUi(gameId)
            }
        } catch (e:Exception){
            Log.e(TAG, "getToHall: ${e.message}", e)
            throw e
        }
        return GameModelData().toModelUi(gameId)
        /*return suspendCancellableCoroutine { cancellableContinuation ->
            firestore.collection(PATH_HALL).document(gameId).get()
                .addOnSuccessListener { document ->
//                    val currentGame = getGameModelData(document).toModelUi(gameId)
                    val currentGame = document.toObject<GameModelData>() ?: GameModelData()
                    Log.i(TAG, "getToHall currentGame $currentGame ")

                    cancellableContinuation.resume(currentGame.toModelUi(gameId))
                }.addOnFailureListener { cancellableContinuation.resumeWithException(it) }
        }*/
    }
    override suspend fun joinToHall(gameId: String): Flow<GameModelUi> = callbackFlow {

        val listener = EventListener<DocumentSnapshot> { snapshot, exception ->

            if (exception != null)  close(exception)
            else if (snapshot != null && snapshot.exists()) {

                try {
                    //val currentGameData = getGameModelData(snapshot).toModelUi(gameId)
                    val currentGameData = snapshot.toObject<GameModelData>() ?: GameModelData()

                    Log.i(TAG, "joinToHall snapshot: ${snapshot.data.toString()}")
                    Log.i(TAG, "joinToHall result: $currentGameData ")

                    trySend(currentGameData.toModelUi(gameId))
                } catch (e:Exception){
                    throw e
                }
            }
        }

        // Agrega el listener al documento
        val registration = firestore.collection(PATH_HALL).document(gameId).addSnapshotListener(listener)

        // Cierre el listener cuando se cancela el flujo
        awaitClose { registration.remove() }
    }
    private fun getGameModelData(snapshot: DocumentSnapshot):GameModelData{

        Log.i(TAG, "getGameModelData snapshot: ${snapshot.data}")

        val hallName_Aux   = snapshot.data?.get("hallName") as? String
        val board_AuxList     = snapshot.data?.get("board") as? List<*>
        val board_Aux = listOf(
            board_AuxList?.get(0) as? Int,
            board_AuxList?.get(1) as? Int,
            board_AuxList?.get(2) as? Int,
            board_AuxList?.get(3) as? Int,
            board_AuxList?.get(4) as? Int,
            board_AuxList?.get(5) as? Int,
            board_AuxList?.get(6) as? Int,
            board_AuxList?.get(7) as? Int,
            board_AuxList?.get(8) as? Int
        )

        val player1_Aux     = snapshot.data?.get("player1") as? Map<*, *>
        val player1 = PlayerModelData(
            userId = player1_Aux?.get("userId") as? String,
            userName = player1_Aux?.get("userName") as? String,
            playerType =  (player1_Aux?.get("playerType") as? Long)?.toInt(),
            victories = (player1_Aux?.get("victories") as? Long)?.toInt(),
            resetGame = player1_Aux?.get("resetGame") as? Boolean,
        )

        val player2_Aux     = snapshot.data?.get("player2") as? Map<*, *>
        val player2 = PlayerModelData(
            userId = player2_Aux?.get("userId") as? String,
            userName = player2_Aux?.get("userName") as? String,
            playerType =  (player2_Aux?.get("playerType") as? Long)?.toInt(),
            victories = (player2_Aux?.get("victories") as? Long)?.toInt(),
            resetGame = player2_Aux?.get("resetGame") as? Boolean,
        )

        val playerTurn_Aux  = snapshot.data?.get("playerTurn") as? Map<*, *>
        val playerTurn = PlayerModelData(
            userId = playerTurn_Aux?.get("userId") as? String,
            userName = playerTurn_Aux?.get("userName") as? String,
            playerType =  (playerTurn_Aux?.get("playerType") as? Long)?.toInt(),
            victories = (playerTurn_Aux?.get("victories") as? Long)?.toInt(),
            resetGame = playerTurn_Aux?.get("resetGame") as? Boolean,
        )

        val isPublic_Aux    = snapshot.data?.get("isPublic") as? Boolean
        val password_Aux    = snapshot.data?.get("password") as? String

        val isFinished_Aux  = snapshot.data?.get("isFinished") as? Boolean
        val isVisible_Aux   = snapshot.data?.get("isVisible") as? Boolean
        val winner_LongAux     = snapshot.data?.get("winner") as? Long?
        val winner_Aux     = winner_LongAux?.toInt() ?: 0



















//        val hallName_Aux   = snapshot.getString("hallName")
//        val board_Aux     = snapshot.get("board") as List<Int?>?
//
//        val player1_Aux     = snapshot.get("player1") as? Map<String, Any>
//        val player1 = PlayerModelData(
//            userId = player1_Aux?.get("userId") as? String,
//            userName = player1_Aux?.get("userName") as? String,
//            playerType =  (player1_Aux?.get("playerType") as? Long)?.toInt(),
//            victories = (player1_Aux?.get("victories") as? Long)?.toInt(),
//            resetGame = player1_Aux?.get("resetGame") as? Boolean,
//        )
//
//        val player2_Aux     = snapshot.get("player2") as? Map<String, Any>
//        val player2 = PlayerModelData(
//            userId = player2_Aux?.get("userId") as? String,
//            userName = player2_Aux?.get("userName") as? String,
//            playerType =  (player2_Aux?.get("playerType") as? Long)?.toInt(),
//            victories = (player2_Aux?.get("victories") as? Long)?.toInt(),
//            resetGame = player2_Aux?.get("resetGame") as? Boolean,
//        )
//
//        val playerTurn_Aux  = snapshot.get("playerTurn") as? Map<String, Any>
//        val playerTurn = PlayerModelData(
//            userId = playerTurn_Aux?.get("userId") as? String,
//            userName = playerTurn_Aux?.get("userName") as? String,
//            playerType =  (playerTurn_Aux?.get("playerType") as? Long)?.toInt(),
//            victories = (playerTurn_Aux?.get("victories") as? Long)?.toInt(),
//            resetGame = playerTurn_Aux?.get("resetGame") as? Boolean,
//        )
//
//        val isPublic_Aux    = snapshot.getBoolean("isPublic")
//        val password_Aux    = snapshot.getString("password")
//
//        val isFinished_Aux  = snapshot.getBoolean("isFinished")
//        val isVisible_Aux   = snapshot.getBoolean("isVisible")
//        val winner_LongAux     = snapshot.get("winner") as? Long?
//        val winner_Aux     = winner_LongAux?.toInt() ?: 0

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
            winner = winner_Aux
        )

        Log.d(TAG, "---------------------- getGameModelData ----------------------")
        Log.d(TAG, "getGameModelData hallName_Aux: $hallName_Aux ")
        Log.d(TAG, "getGameModelData board_Aux: $board_Aux ")
        Log.d(TAG, "getGameModelData player1_Aux: $player1_Aux ")
        Log.d(TAG, "getGameModelData player1: $player1 ")
        Log.d(TAG, "getGameModelData player2_Aux: $player2_Aux ")
        Log.d(TAG, "getGameModelData player2: $player2 ")
        Log.d(TAG, "getGameModelData playerTurn_Aux: $playerTurn_Aux ")
        Log.d(TAG, "getGameModelData playerTurn: $playerTurn ")
        Log.d(TAG, "getGameModelData isPublic_Aux: $isPublic_Aux ")
        Log.d(TAG, "getGameModelData password_Aux: $password_Aux ")
        Log.d(TAG, "getGameModelData isFinished_Aux: $isFinished_Aux ")
        Log.d(TAG, "getGameModelData isVisible_Aux: $isVisible_Aux ")
        Log.d(TAG, "getGameModelData winner_Aux: $winner_Aux ")
        Log.d(TAG, "getGameModelData result: $result ")
        Log.d(TAG, "--------------------------------------------------------------")

        return result
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
            if (exception != null)  close(exception)
            else if (collection != null) {

                val halls:MutableList<GameModelUi> = mutableListOf()
                collection.documents.forEach { snapshot ->
//                    halls.add(getGameModelData(it).toModelUi(it.id))
                    halls.add(snapshot.toObject<GameModelData>()?.toModelUi(snapshot.id) ?: GameModelData().toModelUi(snapshot.id))
                }

                val currentGameData:List<GameModelUi> = halls

                Log.i("erich", "getHalls snapshot: ${collection.documents}")
                Log.i("erich", "getHalls result: $currentGameData ")


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
