package com.example.tateti_20.data.network

import android.net.Uri
import android.util.Log
import com.example.tateti_20.ui.model.UserModelUi
import com.example.tateti_20.ui.theme.string_log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = string_log
class FirebaseStorageService @Inject constructor(private val storage: FirebaseStorage) {

    suspend fun uploadImage(user:UserModelUi, uri: Uri): Boolean {
        return suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            val reference = storage.reference.child("profilePhoto/${user.userEmail}/image")

            reference.putFile(uri, createMetaData(user))
                .addOnSuccessListener{ cancellableContinuation.resume(true)}
                    .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
        }
    }

    suspend fun uploadAndDownloadImage(user:UserModelUi, uri: Uri): Uri {
        return suspendCancellableCoroutine<Uri> { cancellableContinuation ->
            val reference = storage.reference.child("profilePhoto/${user.userEmail}/image")

            reference.putFile(uri, createMetaData(user))
                .addOnSuccessListener{ downloadImage(it, cancellableContinuation) }
                .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot,
        cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl
            .addOnSuccessListener {uri->
                readMetadata(uri)
                cancellableContinuation.resume(uri)
            }
            .addOnFailureListener{ cancellableContinuation.resumeWithException(it) }
    }

    private fun createMetaData(user: UserModelUi): StorageMetadata {
        return storageMetadata {
            contentType = "image/jpeg"
//            setCustomMetadata("date","12-12-12")
            setCustomMetadata("persona",user.userEmail)
        }
    }

    private fun readMetadata(uri: Uri){
        Log.d(TAG, "${uri.lastPathSegment}")

        val reference = storage.reference.child(uri.lastPathSegment.toString())

        val response = reference.metadata.addOnSuccessListener { res ->
            res.customMetadataKeys.forEach {key ->
                res.getCustomMetadata(key)?.let {value ->
                    Log.d(TAG, "key: $key value: $value")
                }
            }

        }
    }

    private fun deleteImage(uri: Uri){
        val reference = storage.reference.child(uri.lastPathSegment.toString())
        val result = reference.delete().isSuccessful
        if(result) Log.d(TAG, "image deleted") else Log.d("deleteImage", "image error")

    }

    suspend fun getAllImages(): List<Uri> {
        val reference = storage.reference.child("download/")
        return reference.listAll().await().items.map { it.downloadUrl.await() }
    }

    suspend fun getProfilePhoto(userEmail: String): Uri? {
        return suspendCancellableCoroutine<Uri?> { cancellableContinuation ->
            val reference = storage.reference.child("profilePhoto/$userEmail/image")

            Log.d(TAG, "getProfilePhoto reference: $reference")
            Log.d(TAG, "getProfilePhoto bucket: ${reference.bucket}")
            Log.d(TAG, "getProfilePhoto name: ${reference.name}")
            Log.d(TAG, "getProfilePhoto path: ${reference.path}")

            reference.downloadUrl
                .addOnSuccessListener { cancellableContinuation.resume(it) }
                .addOnFailureListener { cancellableContinuation.resume(null) }
        }
    }

}