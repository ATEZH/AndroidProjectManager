package com.up.projectmanager.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    suspend fun getUser(): User {
        val userId = auth.currentUser!!.uid
        val documentSnapshot = db.collection("users")
            .document(userId)
            .get()
            .await()
        return User(
            documentSnapshot.id,
            documentSnapshot.getString("firstName") ?: "",
            documentSnapshot.getString("lastName") ?: "",
            documentSnapshot.getString("email") ?: ""
        )
    }
}