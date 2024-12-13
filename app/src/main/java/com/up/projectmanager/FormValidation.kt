package com.up.projectmanager

import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText

class FormValidation {
    fun validateName(input: TextInputEditText): Boolean {
        val inputText = input.text.toString().trim()
        if (TextUtils.isEmpty(inputText)) {
            input.error = "Required"
            return false
        }
        if (!inputText.all { it.isLetter() }) {
            input.error = "Name cannot contain numbers or special characters"
            return false
        }
        return true
    }

    fun validateEmail(input: TextInputEditText): Boolean {
        val inputText = input.text.toString().trim()
        if (TextUtils.isEmpty(inputText)) {
            input.error = "Required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(inputText).matches()) {
            input.error = "Invalid email format"
            return false
        }
        return true
    }

    fun validatePassword(input: TextInputEditText): Boolean {
        val inputText = input.text.toString().trim()
        if (inputText.length < 8) {
            input.error = "Password must be at least 8 characters long"
            return false
        }
        if (!inputText.any { it.isLetter() }) {
            input.error = "Password must contain letters"
            return false
        }
        if (inputText.all { it.isLetter() }) {
            input.error = "Password must contain numbers or special characters"
            return false
        }
        return true
    }

    fun passwordMatches(input1: TextInputEditText, input2: TextInputEditText): Boolean {
        val inputText1 = input1.text.toString().trim()
        val inputText2 = input2.text.toString().trim()
        if (inputText1 != inputText2) {
            input1.error = "Password doesn't match"
            return false
        }
        return true
    }
}