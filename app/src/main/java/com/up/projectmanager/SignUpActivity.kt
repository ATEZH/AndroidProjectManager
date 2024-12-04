package com.up.projectmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUpActivity  : AppCompatActivity() {

    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_up)

        firstNameInput = findViewById(R.id.first_name_input)
        lastNameInput = findViewById(R.id.last_name_input)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        signUpButton = findViewById(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            if (validateForm()) {
                signUp()
                val myIntent = Intent(this, SignInActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(myIntent)
            }
        }
    }

    fun signUp() {
        val text = "Signed up successfully!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this, text, duration)
        toast.show()
        val prefs = getSharedPreferences(
            "ProjectManager",
            Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val user = User(firstNameInput.text.toString(), lastNameInput.text.toString(), emailInput.text.toString())
        val userSer = Json.encodeToString(user)
        editor.putString("user", userSer)
        editor.apply()
    }

    fun loadSignInLayout(v: View) {
        val myIntent = Intent(this, SignInActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(myIntent)
    }

    fun validateForm(): Boolean {
        val validator = FormValidation()
        return validator.validateName(firstNameInput) and
                validator.validateName(lastNameInput) and
                validator.validateEmail(emailInput) and
                validator.validatePassword(passwordInput) and
                validator.passwordMatches(passwordInput, confirmPasswordInput)
    }
}