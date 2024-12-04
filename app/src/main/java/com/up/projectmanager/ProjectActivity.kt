package com.up.projectmanager

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.serialization.json.Json


class ProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.project_overview)
        val projectString = intent.extras?.getString("project")
        Log.d(TAG, "project: $projectString")
        val project = projectString?.let { Json.decodeFromString<Project>(it) }
        val projectName = findViewById<TextView>(R.id.project_name)
        val projectDescription = findViewById<TextView>(R.id.project_description)
        val projectProgress = findViewById<LinearProgressIndicator>(R.id.project_progress)
        val projectMembers: TableLayout = findViewById(R.id.project_members)
        val projectTasks: TableLayout = findViewById(R.id.project_tasks)
        val projectCreatedOn = findViewById<TextView>(R.id.project_created_on)
        val projectDeadline = findViewById<TextView>(R.id.project_deadline)
        if (project != null) {
            projectName.text = project.name
            projectDescription.text = project.description
            var doneTasks = 0
            for (task in project.tasks) {
                if (task.done) doneTasks++
            }
            projectProgress.progress = (doneTasks*100/project.tasks.size)
            for (member in project.members) {
                val tableRow = TableRow(this)
                for (value in member) {
                    val textView = TextView(this)
                    textView.text = value
                    textView.textSize = 16f
                    textView.gravity = Gravity.CENTER
                    val layoutParams1 = TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams1.weight = 1.0f
                    textView.layoutParams = layoutParams1
                    tableRow.addView(textView)
                }
                projectMembers.addView(tableRow)
            }
            for (task in project.tasks) {
                val tableRow1 = TableRow(this)

                val textView1 = TextView(this)
                tableRow1.addView(textView1)
                textView1.text = task.name
                textView1.textSize = 16f
                textView1.gravity = Gravity.CENTER

                val textView2 = TextView(this)
                tableRow1.addView(textView2)
                textView2.text = if (task.done) "Done" else "Pending"
                textView2.textSize = 16f
                textView2.gravity = Gravity.CENTER
                val layoutParams2 = TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                layoutParams2.weight = 1f
                textView1.layoutParams = layoutParams2
                textView2.layoutParams = layoutParams2

                projectTasks.addView(tableRow1)
            }

            projectCreatedOn.text = "Created on: ${project.createdOn}"
            projectDeadline.text = "Deadline on: ${project.deadline}"
        }
    }
}