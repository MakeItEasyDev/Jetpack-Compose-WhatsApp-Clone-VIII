package com.jetpack.whatsappclone.utils

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class WhatsAppResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?): WhatsAppResponse()
data class OnError(val exception: FirebaseFirestoreException?): WhatsAppResponse()
