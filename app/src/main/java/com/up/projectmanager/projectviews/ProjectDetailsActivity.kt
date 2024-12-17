package com.up.projectmanager.projectviews

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.data.user.User
import java.util.*


class ProjectDetailsActivity : AppCompatActivity() {

    private val viewModel: ProjectViewModel by viewModels()
    private lateinit var project: Project
    private lateinit var members: List<User>
    private lateinit var tasks: List<Task>
    private lateinit var loadingSpinner: CircularProgressIndicator
    private lateinit var container: LinearLayout
    private lateinit var projectName: TextView
    private lateinit var projectDescription: TextView
    private lateinit var projectCreatedOn: TextView
    private lateinit var projectDeadline: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var projectMembersTable: TableLayout
    private lateinit var tasksTable: TableLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_overview)
        viewModel.setProjectId(intent.getStringExtra("project")!!)
        viewModel.getProject()
        initializeUI()
        observeViewModel()
    }

    private fun initializeUI() {
        loadingSpinner = findViewById(R.id.loading_spinner)
        container = findViewById(R.id.container)
        projectName = findViewById(R.id.project_name)
        projectDescription = findViewById(R.id.project_description)
        progressBar = findViewById(R.id.project_progress)
        projectMembersTable = findViewById(R.id.project_members)
        tasksTable = findViewById(R.id.project_tasks)
        projectCreatedOn = findViewById(R.id.project_created_on)
        projectDeadline = findViewById(R.id.project_deadline)
    }

    private fun observeViewModel() {
        viewModel.project.observe(this) { project ->
            this.project = project
            showProjectDetails(project)
        }
        viewModel.members.observe(this) { members ->
            this.members = members
        }
        viewModel.tasks.observe(this) { tasks ->
            this.tasks = tasks
        }
        viewModel.loading.observe(this) { loading ->
            setLoading(loading)
        }
    }

    private fun showProjectDetails(project: Project) {
        projectName.text = project.name
        projectDescription.text = project.description
        var completedTasks = 0
        for (task in project.tasks) if (task.completed) completedTasks++
        progressBar.progress = if (project.tasks.isEmpty()) 0 else (completedTasks*100)/project.tasks.size
        for (member in this.members) {
            val tableRow = TableRow(this)
            val memberNameTextView = createTextView("${member.firstName} ${member.lastName}")
            tableRow.addView(memberNameTextView)
            val memberRoleTextView = createTextView(
                project.memberRoles[member.id]!!.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            )
            tableRow.addView(memberRoleTextView)
            tableRow.setPadding(0, 16, 0, 16)
            projectMembersTable.addView(tableRow)
        }

        for (task in this.tasks) {
            val tableRow = TableRow(this)
            val taskNameTextView = createTextView(task.name)
            tableRow.addView(taskNameTextView)
            val taskStatusTextView = createTextView(if (task.completed) "Completed" else "In Progress")
            tableRow.addView(taskStatusTextView)
            val layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f
            taskNameTextView.layoutParams = layoutParams
            taskStatusTextView.layoutParams = layoutParams
            tableRow.setPadding(0, 16, 0, 16)
            tasksTable.addView(tableRow)
        }
        projectCreatedOn.text = "Created On: ${project.createdOn}"
        projectDeadline.text = "Deadline: ${project.deadline}"
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

    private fun setLoading(status: Boolean) {
        loadingSpinner.visibility = if (status) View.VISIBLE else View.INVISIBLE
        container.visibility = if (status) View.INVISIBLE else View.VISIBLE
    }
}