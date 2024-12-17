package com.up.projectmanager.data.project

import com.up.projectmanager.data.task.Task

data class Project(val id: String,
                val name: String,
                val description: String,
                val createdOn: String,
                val deadline: String,
                val members: List<String>,
                val memberRoles: HashMap<String, String>,
                var tasks: List<Task>)