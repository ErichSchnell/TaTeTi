package com.example.tateti_20.di

import android.content.Context
import com.example.tateti_20.data.database.DataStoreService
import com.example.tateti_20.data.network.FirebaseService
import com.example.tateti_20.domain.DataServerService
import com.example.tateti_20.domain.DatabaseLocalService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Singleton
    @Provides
    fun provideFirebaseFireStore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideDataServer(firestore: FirebaseFirestore): DataServerService {
        return FirebaseService(firestore)
    }

    @Singleton
    @Provides
    fun provideDatabaseService(@ApplicationContext context: Context): DatabaseLocalService {
        return DataStoreService(context)
    }


    @Singleton
    @Provides
    fun provideFireAuth() = FirebaseAuth.getInstance()

}