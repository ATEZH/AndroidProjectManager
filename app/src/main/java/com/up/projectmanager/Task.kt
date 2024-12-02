package com.up.projectmanager

import java.util.*

data class Task(val name: String,
                val description: String,
                val assignedTo: List<User>,
                val assignedBy: List<User>,
                val createdOn: Date,
                val deadline: Date,
                val subTasks: List<Task>?)
