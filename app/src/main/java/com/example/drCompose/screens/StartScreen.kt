package com.example.drCompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.drCompose.components.IqButton
import com.example.drCompose.components.IqFillButton
import com.example.drCompose.koin.StringsUtls
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.driqpro.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun StartScreen(params: Any? = null) {

    val stringsUtls : StringsUtls = get()

    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }

    val startCoroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = 16.dp, end = 16.dp)
            .background(Color.White)
    )
    {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            SnackbarHost(hostState = snackbarHostState.value)
        }

        if (params != null && params is String){
            LaunchedEffect(true) {
                startCoroutineScope.launch {
                    snackbarHostState.value.showSnackbar(
                        message = params.toString(),
                        actionLabel = "Okay"
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.compose),
                contentDescription = "NHS Logo",
                modifier = Modifier
                    .fillMaxWidth(10f)
                    .padding(top = 25.dp, end = 15.dp),
                alignment = Alignment.CenterEnd
            )

//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher),
//                contentDescription = "DrIQ Logo",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .size(150.dp)
//                    .padding(top = 50.dp),
//                alignment = Alignment.Center
//            )


            AsyncImage(
                model = "https://molo17.com/wp-content/uploads/2021/11/StudioCompose10.jpg",
                placeholder = painterResource(id = R.drawable.ic_launcher),
                error = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "The delasign logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .padding(top = 25.dp, bottom = 25.dp),
                alignment = Alignment.Center
            )


            IqFillButton(
                stringsUtls.signIn,
                Modifier.align(Alignment.CenterHorizontally), onClick = {
                    DrComposeAppRouter.navigateTo(Screens.Login)
                })

            IqButton(
                stringsUtls.createAccount,
                Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally), onClick = {
                    DrComposeAppRouter.navigateTo(Screens.SignUp)
                })

        }

    }
}

@Preview
@Composable
fun DefaultPreview() {
    StartScreen()
}
