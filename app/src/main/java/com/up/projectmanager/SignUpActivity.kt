package com.up.projectmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

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
            validateForm()
        }
    }

    fun loadSignInLayout(v: View) {
        val myIntent = Intent(this, SignInActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(myIntent)
    }

    fun validateForm(): Boolean {
        val validator = FormValidation()
        var valid = validator.validateName(firstNameInput)
        valid = validator.validateName(lastNameInput)
        valid = validator.validateEmail(emailInput)
        valid = validator.validatePassword(passwordInput)
        valid = validator.passwordMatches(passwordInput, confirmPasswordInput)
        return valid
    }
}