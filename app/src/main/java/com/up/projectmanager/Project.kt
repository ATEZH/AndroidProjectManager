package com.up.projectmanager

import java.util.*
/***
 *
 * name
 * description
 * createdOn
 * deadline
 * tasks
 * projectLead
 * projectMembers
 *
 */
data class Project(val name: String,
                   val description: String,
                   val createdOn: Date,
                   val deadline: Date,
                   val tasks: List<Task>)
