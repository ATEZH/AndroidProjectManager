package com.up.projectmanager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.project.ProjectRepository
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.data.task.TaskRepository
import com.up.projectmanager.data.user.User
import com.up.projectmanager.data.user.UserRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val taskRepository = TaskRepository()
    private val projectRepository = ProjectRepository()
    private val userRepository = UserRepository()

    private val _projectsLoading = MutableLiveData<Boolean>()
    val projectsLoading: LiveData<Boolean> get() = _projectsLoading

    private val _tasksLoading = MutableLiveData<Boolean>()
    val tasksLoading: LiveData<Boolean> get() = _tasksLoading

    private val _userLoading = MutableLiveData<Boolean>()
    val userLoading: LiveData<Boolean> get() = _userLoading

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> get() = _projects

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        getUser()
        fetchProjects()
        fetchUserTasks()
    }

    fun fetchProjects() {
        _projectsLoading.postValue(true)
        viewModelScope.launch {
            try {
                val projects = projectRepository.fetchProjects()
                _projects.postValue(projects)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching projects", e)
            } finally {
                _projectsLoading.postValue(false)
            }
        }
    }

    fun fetchUserTasks() {
        _tasksLoading.postValue(true)
        viewModelScope.launch {
            try {
                val tasks = taskRepository.fetchUserTasks()
                _tasks.postValue(tasks)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching tasks", e)
            } finally {
                _tasksLoading.postValue(false)
            }
        }
    }

    fun getUser() {
        _userLoading.postValue(true)
        viewModelScope.launch {
            try {
                val user = userRepository.getUser()
                _user.postValue(user)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching user", e)
            } finally {
                _userLoading.postValue(false)
            }
        }
    }
}