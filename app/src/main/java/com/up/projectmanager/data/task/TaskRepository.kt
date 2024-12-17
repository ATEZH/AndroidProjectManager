package com.up.projectmanager.data.task

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.user.User
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
    suspend fun getTask(taskId: String): Task {
        val documentSnapshot = db.collection("tasks")
            .document(taskId)
            .get()
            .await()
        return Task(
            id = documentSnapshot.id,
            name = documentSnapshot.getString("name") ?: "",
            description = documentSnapshot.getString("description") ?: "",
            createdOn = timestampConverter.timestampToString(documentSnapshot["createdOn"] as Timestamp),
            deadline = timestampConverter.timestampToString(documentSnapshot["deadline"] as Timestamp),
            assignees = documentSnapshot.get("assignees") as ArrayList<String>,
            completed = documentSnapshot.getBoolean("completed") ?: false,
            projectId = documentSnapshot.getString("projectId") ?: ""
        )
    }

    suspend fun getTaskProject(projectId: String): Project {
        val documentSnapshot = db.collection("projects")
            .document(projectId)
            .get()
            .await()
        return Project(
            id = documentSnapshot.id,
            name = documentSnapshot.getString("name") ?: "",
            description = documentSnapshot.getString("description") ?: "",
            createdOn = timestampConverter.timestampToString(documentSnapshot.getTimestamp("createdOn")),
            deadline = timestampConverter.timestampToString(documentSnapshot.getTimestamp("deadline")),
            members = ArrayList(),
            memberRoles = HashMap(),
            tasks = mutableListOf()
        )
    }

    suspend fun fetchTaskAssignees(assigneeIds: List<String>): List<User> {
        val assignees = ArrayList<User>()
        for (assigneeId in assigneeIds) {
            val documentSnapshot = db.collection("users")
                .document(assigneeId)
                .get()
                .await()
            assignees.add(User(
                documentSnapshot.id,
                documentSnapshot.getString("firstName") ?: "",
                documentSnapshot.getString("lastName") ?: "",
                documentSnapshot.getString("email") ?: ""
            ))
        }
        return assignees
    }
}