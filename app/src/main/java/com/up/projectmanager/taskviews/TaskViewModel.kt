package com.up.projectmanager.taskviews

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.up.projectmanager.data.project.Project
import com.up.projectmanager.data.task.Task
import com.up.projectmanager.data.task.TaskRepository
import com.up.projectmanager.data.user.User
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {
    private lateinit var taskId: String
    private val taskRepository = TaskRepository()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _project = MutableLiveData<Project>()
    val project: LiveData<Project> get() = _project

    private val _assignees = MutableLiveData<List<User>>()
    val assignees: LiveData<List<User>> get() = _assignees

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> get() = _task

    fun setTaskId(taskId: String) {
        this.taskId = taskId
    }

    fun getTask() {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val task = taskRepository.getTask(taskId)
                val project = taskRepository.getTaskProject(task.projectId.trim())
                _project.postValue(project)
                val assignees = taskRepository.fetchTaskAssignees(task.assignees)
                _assignees.postValue(assignees)
                _task.postValue(task)
            } catch(e: Exception) {
                Log.e("MainViewModel", "Error fetching task", e)
            } finally {
                _loading.postValue(false)
            }
        }
    }
}