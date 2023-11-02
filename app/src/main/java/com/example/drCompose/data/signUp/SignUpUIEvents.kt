package com.example.drCompose.data.signUp

import android.net.Uri

sealed class SignUpUIEvents {

    data class OnSelectProfilePhoto(val uri: Uri) : SignUpUIEvents()
    data class OnFirstNameChange(val firstName: String) : SignUpUIEvents()
    data class OnMiddleNameChange(val middleName: String) : SignUpUIEvents()
    data class OnLastNameChange(val lastName: String) : SignUpUIEvents()
    data class OnEmailChange(val email: String) : SignUpUIEvents()
    data class OnPasswordChange(val password: String) : SignUpUIEvents()
    data class OnDateSelection(val date: String) : SignUpUIEvents()
    data class OnGenderSelection(val gender: String) : SignUpUIEvents()

    data object OnSignUpButtonClick : SignUpUIEvents()

}