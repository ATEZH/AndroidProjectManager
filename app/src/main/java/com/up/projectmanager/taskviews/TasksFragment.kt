package com.up.projectmanager.taskviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.up.projectmanager.MainViewModel
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.projectviews.ProjectAdapter

class TasksFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var loadingSpinner: CircularProgressIndicator

    private var taskList = arrayListOf<Task>()
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        initializeUI(view)
        observeViewModel()

        return view
    }

    private fun initializeUI(view: View) {
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        taskAdapter = TaskAdapter(taskList)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = taskAdapter
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            updateTasksList(tasks)
        }
        viewModel.tasksLoading.observe(viewLifecycleOwner) { tasksLoading ->
            setLoading(tasksLoading)
        }
    }

    private fun updateTasksList(tasks: List<Task>) {
        taskAdapter.updateProjects(tasks)
    }

    private fun setLoading(status: Boolean) {
        loadingSpinner.visibility = if (status) View.VISIBLE else View.INVISIBLE
        recyclerView.visibility = if (status) View.INVISIBLE else View.VISIBLE
    }
}