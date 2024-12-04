package com.example.scrollviewui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.up.projectmanager.Project
import com.up.projectmanager.ProjectActivity
import com.up.projectmanager.R
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class ProjectAdapter(private val ProjectList: List<Project>):
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    private var onClickListener: View.OnClickListener? = null
    class ProjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val projectName = itemView.findViewById<TextView>(R.id.project_name)
        val projectDescription = itemView.findViewById<TextView>(R.id.project_description)
        val projectProgress = itemView.findViewById<LinearProgressIndicator>(R.id.project_progress)
        val projectDeadline = itemView.findViewById<TextView>(R.id.project_deadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_card, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = ProjectList[position]
        holder.projectName.text = project.name
        holder.projectDescription.text = project.description
        var doneTasks = 0
        for (task in project.tasks) {
            if (task.done) doneTasks++
        }
        holder.projectProgress.progress = (doneTasks*100/project.tasks.size)
        holder.projectDeadline.text = project.deadline
        holder.itemView.id = project.id.toInt()
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ProjectActivity::class.java)
            val projectSer = Json.encodeToString(project)
            intent.putExtra("project", projectSer)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = ProjectList.size
}