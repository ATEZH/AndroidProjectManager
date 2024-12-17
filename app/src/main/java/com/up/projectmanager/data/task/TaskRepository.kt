package com.up.projectmanager.data.task

import com.google.firebase.Timestamp
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
                id = document.id,
                name = document.getString("name") ?: "",
                description = document.getString("description") ?: "",
                createdOn = timestampConverter.timestampToString(document["createdOn"] as Timestamp),
                deadline = timestampConverter.timestampToString(document["deadline"] as Timestamp),
                assignees = document.get("assignees") as ArrayList<String>,
                completed = document.getBoolean("completed") ?: false,
                projectId = document.getString("projectId") ?: ""
            )
        }
    }


}