package com.up.projectmanager.data.task

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.up.projectmanager.util.ConvertTimestamp
import kotlinx.coroutines.tasks.await

class TaskRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val timestampConverter = ConvertTimestamp()

    suspend fun fetchUserTasks(): List<Task> {
        val userId = auth.currentUser!!.uid
        val querySnapshot = db.collection("tasks")
            .whereArrayContains("assignees", userId)
            .get()
            .await()
        return querySnapshot.map { document ->
            Task(
                name = document.getString("name") ?: "",
                description = document.getString("description") ?: "",
                createdOn = timestampConverter.timestampToString(document["createdOn"]),
                deadline = timestampConverter.timestampToString(document["deadline"]),
                completed = document.getBoolean("completed") ?: false,
                projectId = document.getString("projectId") ?: ""
            )
        }
    }
}