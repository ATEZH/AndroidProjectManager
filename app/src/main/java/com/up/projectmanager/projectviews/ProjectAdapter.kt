package com.up.projectmanager.projectviews

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project

class ProjectAdapter(val projectList: MutableList<Project> = mutableListOf()):
    RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    class ProjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.project_name)
        val projectDescription: TextView = itemView.findViewById(R.id.project_description)
        val projectProgress: LinearProgressIndicator = itemView.findViewById(R.id.project_progress)
        val projectDeadline: TextView = itemView.findViewById(R.id.project_deadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_card, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList[position]
        holder.projectName.text = project.name
        holder.projectDescription.text = project.description
        var completedTasks = 0
        for (task in project.tasks) if (task.completed) completedTasks++
        holder.projectProgress.progress = if (project.tasks.isEmpty()) 0 else (completedTasks*100)/project.tasks.size
        holder.projectDeadline.text = "Deadline: ${project.deadline}"
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ProjectDetailsActivity::class.java)
            intent.putExtra("project", project.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun updateProjects(newProjects: List<Project>) {
        projectList.clear()
        projectList.addAll(newProjects)
        notifyDataSetChanged()
    }

    override fun getItemCount() = projectList.size
}