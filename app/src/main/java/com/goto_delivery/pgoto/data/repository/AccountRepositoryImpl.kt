package com.goto_delivery.pgoto.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.goto_delivery.pgoto.domain.model.User
import com.goto_delivery.pgoto.domain.repository.AccountRepository
import com.goto_delivery.pgoto.ui.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AccountRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AccountRepository {
    override fun updateEmail(newEmail: String): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override fun updateFullName(newName: String): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override fun updateAddress(newAddress: String, city: String): Flow<Resource<Unit>> =
        callbackFlow {
            firestore.collection("users")
                .document(auth.currentUser!!.uid)
                .update("address", newAddress, "city", city)
                .addOnCompleteListener { task ->
                    val result = if (task.isSuccessful) {
                        Resource.Success<Unit>(data = null)
                    } else {
                        Resource.Error<Unit>(exception = task.exception!!)
                    }

                    trySend(result).isSuccess
                }

            awaitClose { close() }
        }

    override fun updatePhoneNumber(newPhoneNumber: String): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override fun getAccountInfo(): Flow<Resource<User>> = callbackFlow {
        val listener = firestore.collection("users")
            .document(auth.uid!!)
            .addSnapshotListener { snapshot, error ->
                val result = if (error == null)
                    Resource.Success<User>(data = snapshot?.toObject())
                else
                    Resource.Error<User>(exception = error)

                trySend(result).isSuccess
            }

        awaitClose { listener.remove() }
    }
}