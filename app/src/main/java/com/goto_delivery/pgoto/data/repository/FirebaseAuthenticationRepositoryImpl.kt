package com.goto_delivery.pgoto.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.goto_delivery.pgoto.data.di.FirebaseModule
import com.goto_delivery.pgoto.domain.repository.FirebaseAuthenticationRepository
import com.goto_delivery.pgoto.ui.utils.Resource
import dagger.Component
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FirebaseAuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : FirebaseAuthenticationRepository {
    override fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> = callbackFlow {

        trySend(Resource.Loading<FirebaseUser>())

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val result = if (task.isSuccessful) {
                    val user = task.result.user
                    Resource.Success<FirebaseUser>(data = user)
                } else {
                    Resource.Error<FirebaseUser>(exception = task.exception!!)
                }

                trySend(result).isSuccess
            }

        awaitClose { cancel() }

    }.flowOn(Dispatchers.IO)

    override fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser>> = callbackFlow {

        trySend(Resource.Loading<FirebaseUser>())

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val result = if (task.isSuccessful) {
                    val user = task.result.user
                    Resource.Success<FirebaseUser>(data = user)
                } else {
                    Resource.Error<FirebaseUser>(exception = task.exception!!)
                }

                trySend(result).isSuccess
            }

        awaitClose { cancel() }

    }.flowOn(Dispatchers.IO)

    override fun continueWithGoogle(idToken: String): Flow<Resource<FirebaseUser>> = callbackFlow {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        trySend(Resource.Loading<FirebaseUser>())

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                val result = if (task.isSuccessful) {
                    Log.d("login", "google sign in success")
                    Resource.Success<FirebaseUser>(task.result.user)
                } else {
                    Log.d("login", "google sign in failed")
                    Resource.Error<FirebaseUser>(task.exception!!)
                }

                trySend(result).isSuccess
            }

        awaitClose { close() }
    }

    override fun continueWithFacebook(): Flow<Resource<FirebaseUser>> {
        TODO("Not yet implemented")
    }

}