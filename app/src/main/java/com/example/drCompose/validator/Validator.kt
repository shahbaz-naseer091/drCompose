package com.example.drCompose.validator

import com.example.drCompose.utils.Constants.PASSWORD_MIN_LENGTH
import com.example.drCompose.utils.Constants.REGEX_EMAIL
import com.example.drCompose.utils.Constants.REGEX_PASSWORD

object Validator {

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(email.matches(REGEX_EMAIL.toRegex()) && email.isNotEmpty(), "Please enter a valid email")
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(password.matches(REGEX_PASSWORD.toRegex()) && password.isNotEmpty() && password.length > PASSWORD_MIN_LENGTH, "Please enter a valid password")
    }

    data class ValidationResult(
        val status: Boolean = false,
        val message : String = ""
    )

}