package com.up.projectmanager.data.project

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.data.user.User
import com.up.projectmanager.util.ConvertTimestamp
import kotlinx.coroutines.tasks.await

class ProjectRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val timestampConverter = ConvertTimestamp()

    suspend fun createProject(project: HashMap<String, Any>): String {
        val documentSnapshot = db.collection("projects")
            .add(project)
            .await()
        return documentSnapshot.id
    }

    suspend fun getProject(projectId: String): Project {
        println(projectId)
        val documentSnapshot = db.collection("projects")
            .document(projectId)
            .get()
            .await()
        val tasks = fetchProjectTasks(documentSnapshot.id)
        return Project(
            id = documentSnapshot.id,
            name = documentSnapshot.getString("name") ?: "",
            description = documentSnapshot.getString("description") ?: "",
            createdOn = timestampConverter.timestampToString(documentSnapshot.getTimestamp("createdOn")),
            deadline = timestampConverter.timestampToString(documentSnapshot.getTimestamp("deadline")),
            members = documentSnapshot["members"] as ArrayList<String>,
            memberRoles = documentSnapshot["memberRoles"] as HashMap<String, String>,
            tasks = tasks
        )
    }

    suspend fun fetchProjects(): List<Project> {
        val userId = auth.currentUser!!.uid
        val querySnapshot = db.collection("projects")
            .whereArrayContains("members", userId)
            .get()
            .await()
        return querySnapshot.map { document ->
            val members = document["members"] as ArrayList<String>
            val memberRoles = document["memberRoles"] as HashMap<String, String>
            Project(
                id = document.id,
                name = document.getString("name") ?: "",
                description = document.getString("description") ?: "",
                createdOn = timestampConverter.timestampToString(document["createdOn"] as Timestamp),
                deadline = timestampConverter.timestampToString(document["deadline"] as Timestamp),
                members = members,
                memberRoles = memberRoles,
                tasks = mutableListOf()
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

    suspend fun fetchProjectMembers(userIds: List<String>): List<User> {
        var users = ArrayList<User>()
        for (userId in userIds) {
            val documentSnapshot = db.collection("users")
                .document(userId)
                .get()
                .await()
            users.add(User(
                documentSnapshot.id,
                documentSnapshot.getString("firstName") ?: "",
                documentSnapshot.getString("lastName") ?: "",
                documentSnapshot.getString("email") ?: ""
            ))
        }
        return users
    }
}