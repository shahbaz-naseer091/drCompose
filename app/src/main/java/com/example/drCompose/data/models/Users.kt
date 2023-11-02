package com.example.drCompose.data.models

import com.google.firebase.database.Exclude

data class Users(

    @get:Exclude
    var id : String = "",

    var profilePicture : String = "",
    var firstName: String? = "",
    var middleName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var password: String? = "",
    var birthDate: String? = "",
    var gender: String? = ""
)