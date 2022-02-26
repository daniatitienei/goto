package com.goto_delivery.pgoto.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.goto_delivery.pgoto.ui.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthenticationRepository {
    fun registerWithEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>>
    fun loginWithEmailAndPassword(email: String, password: String): Flow<Resource<FirebaseUser>>

    fun continueWithGoogle(): Flow<Resource<FirebaseUser>>

    fun continueWithFacebook(): Flow<Resource<FirebaseUser>>
}