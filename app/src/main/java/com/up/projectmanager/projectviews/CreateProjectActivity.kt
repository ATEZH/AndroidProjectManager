package com.up.projectmanager.projectviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.up.projectmanager.MainActivity
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.task.Task
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class CreateProjectActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_project)
        val createProjectButton = findViewById<Button>(R.id.create_project_button)
        createProjectButton.setOnClickListener {
            createProject()
        }
    }

    fun createProject() {
        val intent = Intent(this, MainActivity::class.java)
        val id = "123"
        val name = findViewById<TextInputEditText>(R.id.project_name_input).text.toString()
        val description = findViewById<TextInputEditText>(R.id.project_description_input).text.toString()
        val createdOn = GregorianCalendar().get(Calendar.DATE).toString()
        val deadline = findViewById<TextInputEditText>(R.id.project_deadline_input).text.toString()
        val membersRaw = findViewById<TextInputEditText>(R.id.project_members_input).text.toString().split(";")
        val tasksRaw = findViewById<TextInputEditText>(R.id.project_tasks_input).text.toString().split(";")
//        val membersList = membersRaw.map { it.split(",") }
        val tasksList = createTasksFromInput(tasksRaw)
        var project = Project(
            id = id,
            name = name,
            description = description,
            createdOn = createdOn,
            deadline = deadline,
            members = mutableListOf(),
            tasks = tasksList
        )
        val projectSer = Json.encodeToString(project)
        val prefs = getSharedPreferences(
            "ProjectManager",
            Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("createdProject", projectSer)
        startActivity(intent)
    }

    fun createTasksFromInput(tasksRaw: List<String>): List<Task> {
        return tasksRaw.map { taskData ->
            val taskParts = taskData.split(",")
            val name = taskParts[0].trim()
            val description = taskParts[1].trim()
            val createdOn = GregorianCalendar().get(Calendar.DATE).toString()
            val randomDeadline = "2040-10-10"
            Task(name, description, createdOn, randomDeadline, completed = false, "123")
        }
    }
}