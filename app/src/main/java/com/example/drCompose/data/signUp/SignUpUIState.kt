package com.example.drCompose.data.signUp

import android.net.Uri

data class SignUpUIState(

    var profilePhotoBase64: String? = null,
    var profilePhoto: Uri? = null,
    var firstName: String = "",
    var middleName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",
    var date: String = "",
    var gender: String = "",

    var firstNameError: Boolean = false,
    var middleNameError: Boolean = false,
    var lastNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var dateError: Boolean = false,
    var genderError: Boolean = false

)