package com.up.projectmanager

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignInActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var staySignedIn: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_in)

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        signInButton = findViewById(R.id.sign_in_button)
        staySignedIn = findViewById(R.id.stay_signed_in)
        staySignedIn.setChecked(true)

        val prefs = getSharedPreferences(
            "ProjectManager",
            Context.MODE_PRIVATE)

        if (intent.extras?.getBoolean("AUTO_SIGN_IN") == true) {
            val userSerialized = prefs.getString("user", "null")
            val user = userSerialized?.let { Json.decodeFromString<User>(it) }
            signIn(user)
        }


        signInButton.setOnClickListener {
            if (validateForm()) {
                // will take user from database in future
                val userSerialized = prefs.getString("user", "null")
                if (userSerialized == "null") {
                    val text = "User does not exist!"
                    val duration = Toast.LENGTH_SHORT
                    Toast.makeText(this, text, duration).show()
                    loadSignUpLayout()
                } else {
                    val user = userSerialized?.let { Json.decodeFromString<User>(it) }
                    signIn(user)
                }
            }
        }
    }

    private fun signIn(user: User?) {
        val text = "Signed in successfully!"
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(this, text, duration).show()

        val prefs = getSharedPreferences(
            "ProjectManager",
            Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val userSer = Json.encodeToString(user)
        editor.putBoolean("signedUp", true)
        editor.putString("user", userSer)
        editor.putBoolean("staySignedIn", staySignedIn.isChecked)
        editor.putBoolean("signedIn", true)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("user", userSer)
        startActivity(intent)
    }

    fun loadSignUpLayout() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun validateForm(): Boolean {
        val validator = FormValidation()
        return validator.validateEmail(emailInput) and validator.validatePassword(passwordInput)
    }
}