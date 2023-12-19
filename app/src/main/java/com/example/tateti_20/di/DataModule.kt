package com.example.tateti_20.di

import android.content.Context
import com.example.tateti_20.data.database.DataStoreService
import com.example.tateti_20.data.network.FirebaseService
import com.google.firebase.auth.FirebaseAuth
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

//    @Singleton
//    @Provides
//    fun provideDatabaseReference() = Firebase.database.reference
//
//    @Singleton
//    @Provides
//    fun provideDataServer(reference: DatabaseReference): DataServerService{
//        return FirebaseService(reference)
//    }
//
//    @Singleton
//    @Provides
//    fun provideDatabaseService(@ApplicationContext context: Context): DatabaseLocalService {
//        return DataStoreService(context)
//    }

    @Singleton
    @Provides
    fun provideFireAuth() = FirebaseAuth.getInstance()

}