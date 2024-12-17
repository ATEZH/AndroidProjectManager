package com.up.projectmanager.projectviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.up.projectmanager.MainViewModel
import com.up.projectmanager.R
import com.up.projectmanager.data.project.Project

class ProjectsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var loadingSpinner: CircularProgressIndicator

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_projects, container, false)

        initializeUI(view)
        observeViewModel()

        return view
    }

    private fun initializeUI(view: View) {
        loadingSpinner = view.findViewById(R.id.loading_spinner)
        recyclerView = view.findViewById(R.id.recycler_view)
        projectAdapter = ProjectAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = projectAdapter
    }

    private fun observeViewModel() {
        viewModel.projectsLoading.observe(viewLifecycleOwner, Observer { loading ->
            setLoading(loading)
        })
        viewModel.projects.observe(viewLifecycleOwner, Observer { projects ->
            updateProjectsList(projects)
        })
    }

    private fun updateProjectsList(projects: List<Project>) {
        projectAdapter.updateProjects(projects)
    }

    private fun setLoading(status: Boolean) {
        loadingSpinner.visibility = if (status) View.VISIBLE else View.INVISIBLE
        recyclerView.visibility = if (status) View.INVISIBLE else View.VISIBLE
    }
}
