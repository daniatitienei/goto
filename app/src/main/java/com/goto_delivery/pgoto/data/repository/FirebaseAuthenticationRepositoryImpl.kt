package com.goto_delivery.pgoto.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.goto_delivery.pgoto.domain.model.User
import com.goto_delivery.pgoto.domain.repository.FirebaseAuthenticationRepository
import com.goto_delivery.pgoto.ui.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FirebaseAuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirebaseAuthenticationRepository {
    override fun registerWithEmailAndPassword(
        email: String,
        password: String,
        name: String
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

    private fun createUser(user: FirebaseUser, name: String? = null) {

        val userInfo = User(
            email = user.email!!,
            fullName = user.displayName ?: name!!,
        )

        firestore.collection("users")
            .document(user.uid)
            .set(userInfo)
            .addOnSuccessListener { Log.d("document", "User info created") }
    }

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
                    val user = task.result.user

                    Log.d("login", "google sign in success")

                    createUser(user!!)

                    Resource.Success<FirebaseUser>(user)
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