package com.up.projectmanager.projectviews

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.project.ProjectRepository
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.data.user.User
import kotlinx.coroutines.launch

class ProjectViewModel: ViewModel() {
    private lateinit var projectId: String
    private val projectRepository = ProjectRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _project = MutableLiveData<Project>()
    val project: LiveData<Project> = _project

    private val _members = MutableLiveData<List<User>>()
    val members: LiveData<List<User>> = _members

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _projectCreated = MutableLiveData<String>()
    val projectCreated: LiveData<String> = _projectCreated

    fun setProjectId(projectId: String) {
        this.projectId = projectId
    }

    fun getProject() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val project = projectRepository.getProject(projectId)
                val members = projectRepository.fetchProjectMembers(project.members)
                val tasks = projectRepository.fetchProjectTasks(project.id)
                _members.postValue(members)
                _tasks.postValue(tasks)
                _project.postValue(project)
            } catch(e: Exception) {
                Log.e("MainViewModel", "Error fetching tasks", e)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun createProject(project: HashMap<String, Any>) {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                (project["members"] as MutableList<String>).add(auth.uid!!)
                (project["memberRoles"] as HashMap<String, String>)[auth.uid!!] = "Creator"
                val projectId = projectRepository.createProject(project)
                _projectCreated.postValue(projectId)
            } catch(e: Exception) {
                Log.e("MainViewModel", "Error fetching tasks", e)
            } finally {
                _loading.postValue(false)
            }
        }
    }
}