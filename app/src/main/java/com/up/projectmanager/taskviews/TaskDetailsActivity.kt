package com.up.projectmanager.taskviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.up.projectmanager.MainActivity
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.data.user.User

class TaskDetailsActivity : AppCompatActivity() {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var project: Project
    private lateinit var assignees: List<User>
    private lateinit var task: Task
    private lateinit var taskId: String
    private lateinit var loadingSpinner: CircularProgressIndicator
    private lateinit var container: LinearLayout
    private lateinit var taskName: TextView
    private lateinit var projectName: TextView
    private lateinit var taskDescription: TextView
    private lateinit var taskStatus: LinearProgressIndicator
    private lateinit var taskCreatedOn: TextView
    private lateinit var taskDeadline: TextView
    private lateinit var taskAssigneesTable: TableLayout
    private lateinit var markAsCompletedButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_overview)
        taskId = intent.getStringExtra("task")!!
        viewModel.getTask(taskId)
        initializeUI()
        observeViewModel()
    }

    private fun initializeUI() {
        loadingSpinner = findViewById(R.id.loading_spinner)
        container = findViewById(R.id.container)
        taskName = findViewById(R.id.task_name)
        projectName = findViewById(R.id.task_project_name)
        taskDescription = findViewById(R.id.task_description)
        taskStatus = findViewById(R.id.task_status)
        taskAssigneesTable = findViewById(R.id.task_assignees)
        taskDeadline = findViewById(R.id.task_deadline)
        taskCreatedOn = findViewById(R.id.task_created_on)
        markAsCompletedButton = findViewById(R.id.mark_as_completed)
        markAsCompletedButton.setOnClickListener {
            markAsComplete()
        }
    }

    private fun observeViewModel() {
        viewModel.project.observe(this) { project ->
            this.project = project
        }
        viewModel.assignees.observe(this) { assignees ->
            this.assignees = assignees
        }
        viewModel.task.observe(this) { task ->
            this.task = task
            showProjectDetails(task)
        }
        viewModel.taskUpdated.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        viewModel.loading.observe(this) { loading ->
            setLoading(loading)
        }
    }

    private fun showProjectDetails(task: Task) {
        taskName.text = task.name
        projectName.text = "Project: ${project.name}"
        taskDescription.text = task.description
        if (task.completed) {
            taskStatus.trackColor = R.color.green
            taskStatus.progress = 100
        } else {
            taskStatus.trackColor = R.color.secondary_yellow
            taskStatus.progress = 0
        }
        for (assignee in this.assignees) {
            val tableRow = TableRow(this)
            val memberNameTextView = createTextView("${assignee.firstName} ${assignee.lastName}")
            tableRow.addView(memberNameTextView)
            tableRow.setPadding(0, 16, 0, 16)
            taskAssigneesTable.addView(tableRow)
        }
        taskCreatedOn.text = "Created On: ${task.createdOn}"
        taskDeadline.text = "Deadline: ${task.deadline}"
    }

    private fun createTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 16f
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT).apply {
                weight = 1f
            }
        }
    }

    private fun markAsComplete() {
        viewModel.markTaskAsComplete(taskId)
    }

    private fun setLoading(status: Boolean) {
        loadingSpinner.visibility = if (status) View.VISIBLE else View.INVISIBLE
        container.visibility = if (status) View.INVISIBLE else View.VISIBLE
    }
}