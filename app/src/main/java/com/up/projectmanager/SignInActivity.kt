package com.up.projectmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignInActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText;
    private lateinit var passwordInput: TextInputEditText;
    private lateinit var confirmPasswordInput: TextInputEditText;
    private lateinit var signInButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sign_in)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        signInButton = findViewById(R.id.sign_in_button)

        signInButton.setOnClickListener {
            validateForm()
        }
    }

    fun loadSignUpLayout(v: View) {
        val myIntent = Intent(this, SignUpActivity::class.java)
        myIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(myIntent)
    }

    fun validateForm(): Boolean {
        val validator = FormValidation()
        var valid = validator.validateEmail(emailInput)
        valid = validator.validatePassword(passwordInput)
        valid = validator.passwordMatches(passwordInput, confirmPasswordInput)
        return valid
    }
}