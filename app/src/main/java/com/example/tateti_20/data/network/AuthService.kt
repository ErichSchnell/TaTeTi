package com.example.tateti_20.data.network

import android.content.Context
import com.example.tateti_20.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthService @Inject constructor(private val firebaseAuth: FirebaseAuth, @ApplicationContext private val context: Context) {

    /*
      ----------------------- EMAIL -----------------------
     */
    suspend fun login(email: String, password: String): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = it.user
                    cancellableContinuation.resume(user)
                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }
        }
    }
    suspend fun register(email: String, password: String): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user)
                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }
        }
    }
    suspend fun changePassword(email: String): Boolean {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    cancellableContinuation.resume(true)
                }
                .addOnFailureListener {
                    cancellableContinuation.resumeWithException(it)
                }
        }
    }
    /*
      ---------------------------------------------------
     */


    /*
      ----------------------- GOOGLE -----------------------
     */
    fun getGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(context,gso)
    }
    suspend fun loginWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return completeRegisterWithCredential(credential)
    }/*
      ------------------------------------------------------
     */


    /*
      ----------------------- GENERAL -----------------------
     */
    fun isUserLogged(): Boolean {
        return getCurrentUser() != null
    }
    fun logout() {
        firebaseAuth.signOut()
    }
    private fun getCurrentUser() = firebaseAuth.currentUser
    private suspend fun completeRegisterWithCredential(credential: AuthCredential): FirebaseUser?{
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
                cancellableContinuation.resume(it.user)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }
    /*
      ---------------------------------------------------
     */
}