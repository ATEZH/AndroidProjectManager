package com.up.projectmanager.entryviews

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.up.projectmanager.util.FormValidation
import com.up.projectmanager.R

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

    private fun signUp() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                whenUserSignedUp(task.isSuccessful)
            }
    }

    private fun whenUserSignedUp(taskIsSuccessful: Boolean) {
        if (taskIsSuccessful) {
            auth.signOut()
            Toast.makeText(
                baseContext, "Signed up successfully.", Toast.LENGTH_SHORT
            ).show()
            loadSignInLayout()
        } else {
            Toast.makeText(
                baseContext, "Sign up failed.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addUserToDatabase() {
        val firstName = firstNameInput.text.toString().trim()
        val lastName = lastNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val db = Firebase.firestore
        val users = db.collection("users")
        users.document().set(
            hashMapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email,
            )
        )
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