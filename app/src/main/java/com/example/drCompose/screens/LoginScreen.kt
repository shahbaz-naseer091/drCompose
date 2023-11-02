package com.example.drCompose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drCompose.components.Appbar
import com.example.drCompose.components.HeadingInputField
import com.example.drCompose.components.HeadingInputPassword
import com.example.drCompose.components.IqFillButton
import com.example.drCompose.data.login.LoginUIEvents
import com.example.drCompose.data.login.LoginViewModel
import com.example.drCompose.data.login.showSnackbar
import com.example.drCompose.koin.StringsUtls
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.drCompose.navigation.SystemBackButtonHandler
import com.example.drCompose.ui.ui.theme.Blue40
import com.example.drCompose.ui.ui.theme.White
import com.example.driqpro.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel


@Composable
fun LoginScreen() {

    val loginViewModel: LoginViewModel = getViewModel()
    val stringsUtils : StringsUtls = get()

    loginViewModel.apply {
        loginCoroutineScope = rememberCoroutineScope()
        LoginScreenContent(loginViewModel = this, stringsUtils = stringsUtils)
        SnackbarHostRegister(loginViewModel = this)
        ProgressIndicator(loginViewModel = this)
    }

}

@Composable
private fun ProgressIndicator(loginViewModel: LoginViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SnackbarHostRegister(loginViewModel: LoginViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SnackbarHost(hostState = loginViewModel.snackbarHostState.value)
    }
}


@Composable
fun LoginScreenContent(loginViewModel: LoginViewModel, stringsUtils: StringsUtls) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Appbar(endImage = R.drawable.compose, iconVisibility = true) {
            DrComposeAppRouter.navigateTo(Screens.Start)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
                .background(White),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = "DrIQ Logo",
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .fillMaxHeight(0.25f)
                        .align(Alignment.CenterHorizontally)
                        .background(White)
                )

                Text(
                    text = stringsUtils.welcomeToDrIQPleaseSignIn,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    color = Blue40,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )

                Column(modifier = Modifier.fillMaxWidth()) {

                    //Email Address
                    HeadingInputField(
                        heading = stringsUtils.email,
                        hint = stringsUtils.typeYourEmailAddress,
                        modifier = Modifier.padding(top = 24.dp),
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvents.OnEmailChanage(it))
                        },
                        errorStatus = loginViewModel.loginUiState.value.emailError
                    )

                    //password
                    HeadingInputPassword(
                        heading = stringsUtils.password,
                        hint = stringsUtils.typeYourPassword,
                        modifier = Modifier.padding(top = 8.dp),
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvents.OnPasswordChanage(it))
                        },
                        errorStatus = loginViewModel.loginUiState.value.passwordError,
                    )


                    IqFillButton(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = stringsUtils.signIn,
                        onClick = {
                            loginViewModel.apply {
                                if (allValidationPassed.value) {
                                    onEvent(LoginUIEvents.LoginButtonClicked)
                                } else {
                                    loginCoroutineScope?.launch {
                                        showSnackbar(errorMessage.value)
                                    }
                                }
                            }

                        },
                    )


                    Text(
                        text = stringsUtils.forgotEmailAddressOrPassword,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        fontSize = 16.sp,
                        color = Blue40,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light,
                    )


                }

            }


        }

        SystemBackButtonHandler {
            DrComposeAppRouter.navigateTo(Screens.Start)
        }


    }

}


@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}