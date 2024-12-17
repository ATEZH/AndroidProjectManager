package com.up.projectmanager.entryviews

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.up.projectmanager.*
import com.up.projectmanager.util.FormValidation

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var staySignedIn: CheckBox
    private lateinit var goToSignUpButton: TextView
    private val preferences by lazy {
        getSharedPreferences("ProjectManager", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        signInButton = findViewById(R.id.sign_in_button)
        staySignedIn = findViewById(R.id.stay_signed_in)
        staySignedIn.setChecked(true)
        goToSignUpButton = findViewById(R.id.signup2)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null || !preferences.getBoolean("staySignedIn", false)) {
            auth.signOut()
        } else {
            loadMainActivity()
        }

        goToSignUpButton.setOnClickListener {
            loadSignUpLayout()
        }

        signInButton.setOnClickListener {
            if (validateForm()) {
                signIn()
            }
        }
    }

    private fun signIn() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                whenUserSignedIn(task.isSuccessful)
            }
    }

    private fun whenUserSignedIn(taskIsSuccessful: Boolean) {
        if (taskIsSuccessful) {
            val editor = preferences.edit()
            editor.putBoolean("staySignedIn", staySignedIn.isChecked)
            editor.apply()
            Toast.makeText(
                baseContext, "Signed in successfully.", Toast.LENGTH_SHORT
            ).show()
            loadMainActivity()
        } else {
            Toast.makeText(
                baseContext, "Authentication failed.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun loadSignUpLayout() {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun validateForm(): Boolean {
        val validator = FormValidation()
        return validator.validateEmail(emailInput)
    }
}