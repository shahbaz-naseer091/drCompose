package com.example.drCompose.data.login

sealed class LoginUIEvents {

    data class OnEmailChanage (val email: String) : LoginUIEvents()
    data class OnPasswordChanage (val password: String) : LoginUIEvents()

    object LoginButtonClicked : LoginUIEvents()

}