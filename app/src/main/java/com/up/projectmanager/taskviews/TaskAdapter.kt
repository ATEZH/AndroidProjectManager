package com.up.projectmanager.taskviews

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.util.ConvertTimestamp

class TaskAdapter(val taskList: MutableList<Task> = mutableListOf()):
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val timestampConverter = ConvertTimestamp()

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val taskStatus: TextView = itemView.findViewById(R.id.task_status)
        val taskDeadline: TextView = itemView.findViewById(R.id.task_deadline)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_card, parent, false)
        return TaskViewHolder(view)
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.name
        holder.taskDescription.text = task.description
        holder.taskStatus.text = if (task.completed) "Completed" else "In Progress"
        holder.taskDeadline.text = timestampConverter.timestampToString(task.deadline)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, TaskDetailsActivity::class.java)
            intent.putExtra("task", task)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun updateProjects(newTasks: List<Task>) {
        taskList.clear()
        taskList.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun getItemCount() = taskList.size
}