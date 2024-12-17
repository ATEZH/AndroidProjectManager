package com.up.projectmanager.data.task

data class Task(val id: String,
                val name: String,
                val description: String,
                val createdOn: String,
                val deadline: String,
                val assignees: List<String>,
                val completed: Boolean,
                var projectId: String)