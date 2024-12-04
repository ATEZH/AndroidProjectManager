package com.up.projectmanager
import kotlinx.serialization.Serializable

@Serializable
data class User(val firstName: String,
                val lastName: String,
                val email: String)
