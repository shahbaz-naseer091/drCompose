package com.example.drCompose.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.drCompose.koin.PreferenceManager
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.drCompose.screens.HomeScreen
import com.example.drCompose.screens.LoginScreen
import com.example.drCompose.screens.SignUpScreen
import com.example.drCompose.screens.StartScreen
import org.koin.androidx.compose.inject

@Composable
fun DrComposeApp() {

    val preferences by inject<PreferenceManager>()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = White
    ) {

        Crossfade(targetState = DrComposeAppRouter.currentScreen, label = "") { currentState ->

            when (currentState.value) {

                is Screens.SignUp -> {
                    SignUpScreen()
                }

                is Screens.Login -> {
                    LoginScreen()
                }

                is Screens.Start -> {
                    if (preferences.getIsLoggedIn())
                        HomeScreen()
                    else
                        StartScreen()
                }

                else -> {
                    HomeScreen()
                }

            }
        }


    }

}