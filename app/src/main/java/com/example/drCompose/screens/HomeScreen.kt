package com.example.drCompose.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.drCompose.components.Appbar
import com.example.drCompose.koin.PreferenceManager
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.drCompose.navigation.SystemBackButtonHandler
import com.example.driqpro.R
import org.koin.androidx.compose.get

@Composable
fun HomeScreen() {

    val prefManager: PreferenceManager = get()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    ) {

        Appbar(
            endImage = R.drawable.compose,
            title = stringsUtls.homeScreen,
            iconVisibility = true
        ) {
            prefManager.saveIsLoggedIn(false)
            DrComposeAppRouter.navigateTo(Screens.Start)
        }
    }

    SystemBackButtonHandler {
        prefManager.saveIsLoggedIn(false)
        DrComposeAppRouter.navigateTo(Screens.Start)
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}