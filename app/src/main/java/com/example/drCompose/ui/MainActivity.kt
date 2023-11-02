package com.example.drCompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.drCompose.app.DrComposeApp
import com.example.drCompose.ui.ui.theme.DrIQProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DrIQProTheme {
                DrComposeApp()
            }
        }
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        DrComposeApp()
    }


}

