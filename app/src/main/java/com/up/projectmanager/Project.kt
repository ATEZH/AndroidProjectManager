package com.up.projectmanager

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Project(val id: String,
                   val name: String,
                   val description: String,
                   val createdOn: String,
                   val deadline: String,
                   val members: List<List<String>>,
                   val tasks: List<Task>)
