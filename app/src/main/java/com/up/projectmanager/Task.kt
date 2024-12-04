package com.up.projectmanager

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Task(val name: String,
                val description: String,
                val createdOn: String,
                val deadline: String,
                val done: Boolean)

/***
 * progress = (done tasks) / (number of subTasks) if (number of subTasks > 0) or 0 , 1 if number of subTasks = 0;
 */