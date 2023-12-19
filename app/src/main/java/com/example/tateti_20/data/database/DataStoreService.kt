package com.example.tateti_20.data.database

import android.content.Context
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
    }
    private val Context.userPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user"
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
}