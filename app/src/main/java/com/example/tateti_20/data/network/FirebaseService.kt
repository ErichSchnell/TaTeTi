package com.example.tateti_20.data.network

import android.util.Log
import com.example.tateti_20.data.network.model.AnnotatorHeadModelData
import com.example.tateti_20.data.network.model.GameModelData
import com.example.tateti_20.data.network.model.PlayerModelData
import com.example.tateti_20.data.network.model.UserModelData
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.ui.model.AnnotatorHeadModelUi
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
        private const val PATH_ANNOTATOR_HEAD = "annotatorHead"
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
    }
    override suspend fun updateUser(userId: String, userData: UserModelData): Boolean {
        try {
            firestore.collection(PATH_USER).document(userId).set(userData).await()
            return true
        } catch (e:Exception){
            Log.e(TAG, "updateUser: ${e.message}", e)
            throw e
        }
    }
    override suspend fun getUser(userId: String): UserModelUi? {
        try {
            val snapshot = firestore.collection(PATH_USER).document(userId).get().await()
            Log.d(TAG, "getUser snapshot: ${snapshot.data.toString()}")
            if (snapshot.exists()){
                return snapshot.toObject<UserModelData>()?.toModelUi(userId)
            }
        } catch (e:Exception){
            Log.e(TAG, "getUser: ${e.message}", e)
            throw e
        }
        return null
    }
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
    }
    override suspend fun updateToGame(hallId: String, gameModelData: GameModelData):Boolean {
        try {
            firestore.collection(PATH_HALL).document(hallId).set(gameModelData).await()
            return true
        } catch (e:Exception){
            Log.e(TAG, "updateToGame: ${e.message}", e)
            throw e
        }
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
    }
    override suspend fun joinToHall(gameId: String): Flow<GameModelUi> = callbackFlow {

        val listener = EventListener<DocumentSnapshot> { snapshot, exception ->

            if (exception != null)  close(exception)
            else if (snapshot != null && snapshot.exists()) {

                try {
                    val currentGameData = snapshot.toObject<GameModelData>() ?: GameModelData()
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
                    halls.add(snapshot.toObject<GameModelData>()?.toModelUi(snapshot.id) ?: GameModelData().toModelUi(snapshot.id))
                }

                val currentGameData:List<GameModelUi> = halls
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


    /*
    ------------------ ANNOTATOR GAMES ------------------
    */
    override fun getAnnotatorGames(userEmail:String): Flow<List<AnnotatorHeadModelUi>> = callbackFlow {
        // Define el listener para los cambios en el documento

        val listener = EventListener<QuerySnapshot> { collection, exception ->
            if (exception != null)  close(exception)
            else if (collection != null) {

                val annotatorHeads:MutableList<AnnotatorHeadModelUi> = mutableListOf()
                collection.documents.forEach { snapshot ->
                    annotatorHeads.add(snapshot.toObject<AnnotatorHeadModelData>()?.toModelUi() ?: AnnotatorHeadModelData().toModelUi())
                }

                val currentGameData:List<AnnotatorHeadModelUi> = annotatorHeads
                trySend(currentGameData)
            }
        }

        // Agrega el listener al documento
        val registration = firestore.collection("$PATH_ANNOTATOR_HEAD/$userEmail").addSnapshotListener(listener)

        // Cierre el listener cuando se cancela el flujo
        awaitClose { registration.remove() }
    }
    /*
    * --------------------------------------
    * */

}