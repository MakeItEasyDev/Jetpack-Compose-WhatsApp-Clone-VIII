package com.jetpack.whatsappclone.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.jetpack.whatsappclone.utils.OnError
import com.jetpack.whatsappclone.utils.OnSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class WhatsAppRepository {
    private val fireStore = FirebaseFirestore.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getChatList() = callbackFlow {
        val collection = fireStore.collection("whatsapplist") //put here, firebase-firestore collection name
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccess(value)
            } else {
                OnError(error)
            }
            this.trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}