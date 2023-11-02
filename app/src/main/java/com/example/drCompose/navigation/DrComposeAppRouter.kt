package com.example.drCompose.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screens{

    data object Start: Screens()
    data object Home: Screens()
    data object Login: Screens()
    data object SignUp: Screens()


}

object DrComposeAppRouter {

    var currentScreen: MutableState<Screens> = mutableStateOf(Screens.Start)

    fun navigateTo(destination: Screens){
        currentScreen.value = destination
    }


}