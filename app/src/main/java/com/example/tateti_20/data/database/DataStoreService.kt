package com.example.tateti_20.data.database

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tateti_20.domain.DatabaseLocalService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreService @Inject constructor(private val context: Context) : DatabaseLocalService {
    companion object{
        private val USER_ID = stringPreferencesKey("userid")
        private val PROFILE_PHOTO_ID = stringPreferencesKey("profilePhoto")
    }
    private val Context.userPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user"
    )
    private val Context.profilePhotoPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "profilePhoto"
    )

    override suspend fun saveUserId(userId: String) {
        context.userPreferenceDataStore.edit {preferences ->
            preferences[USER_ID] = userId
        }
    }
    override fun getUserId(): Flow<String> {
        return context.userPreferenceDataStore.data.map { preferences ->
            preferences[USER_ID] ?: ""
        }
    }





    override suspend fun setProfilePhotoState(state: Boolean) {
        context.profilePhotoPreferenceDataStore.edit {preferences ->
            preferences[PROFILE_PHOTO_ID] = if (state) "true" else ""
        }
    }
    override fun getProfilePhotoState(): Flow<Boolean> {
        return context.profilePhotoPreferenceDataStore.data.map { preferences ->
            (preferences[PROFILE_PHOTO_ID] != "")
        }
    }
    override suspend fun saveProfilePhoto(uriImage: Uri) {
        context.profilePhotoPreferenceDataStore.edit {preferences ->
            preferences[PROFILE_PHOTO_ID] = uriImage.toString()
        }
    }
    override fun getProfilePhoto(): Flow<Uri> {
        return context.profilePhotoPreferenceDataStore.data.map { preferences ->
            Uri.parse(preferences[PROFILE_PHOTO_ID] ?: "")
        }
    }
}
