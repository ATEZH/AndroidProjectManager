package com.up.projectmanager.data.project

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.util.ConvertTimestamp
import kotlinx.coroutines.tasks.await

class ProjectRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val timestampConverter = ConvertTimestamp()

    suspend fun fetchProjects(): List<Project> {
        val userId = auth.currentUser!!.uid
        val querySnapshot = db.collection("projects")
            .whereArrayContains("members", userId)
            .get()
            .await()
        return querySnapshot.map { document ->
            val taskList = fetchProjectTasks(document.id)
            val members = document["members"] as ArrayList<String>
            Project(
                id = document.id,
                name = document.getString("name") ?: "",
                description = document.getString("description") ?: "",
                createdOn = timestampConverter.timestampToString(document["createdOn"]),
                deadline = timestampConverter.timestampToString(document["deadline"]),
                members = members,
                tasks = taskList
            )
        }
    }

    suspend fun fetchProjectTasks(projectId: String): List<Task> {
        val querySnapshot = db.collection("tasks")
            .whereEqualTo("projectId", projectId)
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