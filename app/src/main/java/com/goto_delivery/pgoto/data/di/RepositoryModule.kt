package com.goto_delivery.pgoto.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.goto_delivery.pgoto.data.repository.FirebaseAuthenticationRepositoryImpl
import com.goto_delivery.pgoto.domain.repository.FirebaseAuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthenticationRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseAuthenticationRepository =
        FirebaseAuthenticationRepositoryImpl(auth, firestore)
}