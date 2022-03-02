package com.goto_delivery.pgoto.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.goto_delivery.pgoto.domain.model.Restaurant
import com.goto_delivery.pgoto.domain.repository.RestaurantRepository
import com.goto_delivery.pgoto.ui.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RestaurantRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RestaurantRepository {

    override fun getRestaurants(): Flow<Resource<List<Restaurant>>> = callbackFlow {

        trySend(Resource.Loading<List<Restaurant>>())

        val listener = firestore.collection("restaurants")
            .addSnapshotListener { snapshot, error ->
                val result = if (error == null)
                    Resource.Success<List<Restaurant>>(data = snapshot?.toObjects() ?: emptyList())
                else
                    Resource.Error<List<Restaurant>>(exception = error)

                trySend(result).isSuccess
            }

        awaitClose {
            listener.remove()
        }
    }

    override fun getRestaurantById(): Flow<Resource<Restaurant>> {
        TODO("Not yet implemented")
    }
}