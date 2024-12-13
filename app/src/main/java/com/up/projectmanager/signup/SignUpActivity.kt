package com.up.projectmanager.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.up.projectmanager.FormValidation
import com.up.projectmanager.MainActivity
import com.up.projectmanager.R
import com.up.projectmanager.User
import com.up.projectmanager.signin.SignInActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SignUpActivity  : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var signUpButton: Button
    private lateinit var goToSignInButton: TextView

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
        goToSignInButton = findViewById(R.id.signin2)
        auth = FirebaseAuth.getInstance()

        goToSignInButton.setOnClickListener {
            loadSignInLayout()
        }

        signUpButton.setOnClickListener {
            if (validateForm()) {
                signUp()
            }
        }
    }

    fun signUp() {
        val firstName = firstNameInput.text.toString().trim()
        val lastName = lastNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.signOut()
                    loadSignInLayout()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Sign up failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun loadSignInLayout() {
        val myIntent = Intent(this, SignInActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(myIntent)
    }

    private fun validateForm(): Boolean {
        val validator = FormValidation()
        return validator.validateName(firstNameInput) and
                validator.validateName(lastNameInput) and
                validator.validateEmail(emailInput) and
                validator.validatePassword(passwordInput) and
                validator.passwordMatches(passwordInput, confirmPasswordInput)
    }
}