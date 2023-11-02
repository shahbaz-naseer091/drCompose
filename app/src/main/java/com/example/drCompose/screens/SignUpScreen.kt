package com.example.drCompose.screens

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.drCompose.components.Appbar
import com.example.drCompose.components.HeadingInputField
import com.example.drCompose.components.HeadingInputPassword
import com.example.drCompose.components.IqFillButton
import com.example.drCompose.components.ProfilePhoto
import com.example.drCompose.components.SelectionInputField
import com.example.drCompose.data.signUp.SignUpUIEvents
import com.example.drCompose.data.signUp.SignUpViewModel
import com.example.drCompose.koin.StringsUtls
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.drCompose.navigation.SystemBackButtonHandler
import com.example.drCompose.utils.Constants
import com.example.drCompose.utils.Extensions
import com.example.drCompose.utils.Extensions.DateSelectionDialog
import com.example.drCompose.utils.Extensions.createImageFile
import com.example.driqpro.R
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

var cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>? = null
var galleryLauncher: ManagedActivityResultLauncher<Uri, Boolean>? = null
var permissionLauncher: ManagedActivityResultLauncher<String, Boolean>? = null
var uri: Uri? = null
var signUpCoroutineScope: CoroutineScope? = null

lateinit var stringsUtls: StringsUtls

@Composable
fun SignUpScreen() {

    val signUpViewModel: SignUpViewModel = getViewModel()

    stringsUtls = get()

    signUpCoroutineScope = rememberCoroutineScope()
    signUpViewModel.contentResolver = LocalContext.current.contentResolver

    val context = LocalContext.current

    cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        signUpViewModel.onEvent(SignUpUIEvents.OnSelectProfilePhoto(uri!!))
    }

    permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            uri = context.createImageFile()
            cameraLauncher?.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    signUpViewModel.apply {
        SignUpScreenContent(signUpViewModel = this)
        SnackbarHostRegister(signUpViewModel = this)
        ProgressIndicator(signUpViewModel = this)

        GenderDialogSelection(signUpViewModel = this)
        AttachmentDialogSelection(signUpViewModel = this, context = LocalContext.current)
    }

}

@Composable
private fun AttachmentDialogSelection(
    signUpViewModel: SignUpViewModel,
    context: Context
) {
    if (signUpViewModel.isAttachmentDialogOpen.value) {
        Extensions.AttachmentDialog(source = stringsUtls) {
            when (it) {
                Constants.CAMERA -> {
                    context.onCaptureImage()
                }

                Constants.GALLERY -> {}
            }
            signUpViewModel.isAttachmentDialogOpen.value = false
        }
    }
}

@Composable
private fun GenderDialogSelection(signUpViewModel: SignUpViewModel) {
    if (signUpViewModel.isGenderDialogOpen.value) {
        signUpViewModel.isGenderDialogOpen.apply {
            Extensions.GenderSelectionDialog(source = stringsUtls, onDismiss = {
                signUpViewModel.onEvent(SignUpUIEvents.OnGenderSelection(it))
                this.value = false
            })
        }
    }
}

@Composable
private fun ProgressIndicator(signUpViewModel: SignUpViewModel) {
    if (signUpViewModel.signUpInProgress.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun SnackbarHostRegister(signUpViewModel: SignUpViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SnackbarHost(hostState = signUpViewModel.snackbarHostState.value)
    }
}


fun Context.onCaptureImage() {

    val permissionCheckResult =
        ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        uri = createImageFile()
        cameraLauncher?.launch(uri)
    } else {
        permissionLauncher?.launch(android.Manifest.permission.CAMERA)
    }

}


@Composable
fun SignUpScreenContent(signUpViewModel: SignUpViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    ) {

        Appbar(
            endImage = R.drawable.compose,
            title = stringsUtls.createAccount,
            iconVisibility = true
        ) {
            DrComposeAppRouter.navigateTo(Screens.Start)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.8f)
                .padding(start = 16.dp, end = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                ProfilePhoto(
                    stringsUtls.addProfilePhoto,
                    uri = signUpViewModel.signUpUIState.value.profilePhoto
                ) {
                    signUpViewModel.isAttachmentDialogOpen.value = true
                }


                //first name
                HeadingInputField(
                    heading = stringsUtls.firstName,
                    hint = stringsUtls.typeFirstNameHere,
                    modifier = Modifier.padding(top = 16.dp),
                    onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvents.OnFirstNameChange(it))
                    }
                )

                //middle name
                HeadingInputField(
                    heading = stringsUtls.middleName + " (Optional)",
                    hint = stringsUtls.typeMiddleNameHere,
                    modifier = Modifier.padding(top = 8.dp),
                    onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvents.OnMiddleNameChange(it))
                    }
                )
                //last name
                HeadingInputField(
                    heading = stringsUtls.lastName,
                    hint = stringsUtls.typeLastNameHere,
                    modifier = Modifier.padding(top = 8.dp), onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvents.OnLastNameChange(it))
                    }
                )

                //Email Address
                HeadingInputField(
                    heading = stringsUtls.email,
                    hint = stringsUtls.typeYourEmailAddress,
                    modifier = Modifier.padding(top = 8.dp), onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvents.OnEmailChange(it))
                    }
                )

                //password
                HeadingInputPassword(
                    heading = stringsUtls.password,
                    hint = stringsUtls.typeYourPassword,
                    modifier = Modifier.padding(top = 8.dp),
                    onTextChanged = {
                        signUpViewModel.onEvent(SignUpUIEvents.OnPasswordChange(it))
                    },
                )


                //date of birth
                SelectionInputField(
                    heading = stringsUtls.dateOfBirth,
                    hint = signUpViewModel.signUpUIState.value.date.ifEmpty { stringsUtls.dateOfBirth },
                ) {
                    signUpViewModel.isDatePickerOpen.value = true
                }

                if (signUpViewModel.isDatePickerOpen.value) {

                    DateSelectionDialog(onDateSelected = {
                        signUpViewModel.apply {
                            isDatePickerOpen.value = false
                            onEvent(SignUpUIEvents.OnDateSelection(it))
                        }
                    })

                }

                SelectionInputField(
                    heading = stringsUtls.gender,
                    hint = signUpViewModel.signUpUIState.value.gender.ifEmpty { stringsUtls.selectGender },
                ) {
                    signUpViewModel.isGenderDialogOpen.value = true
                }

            }

        }

        IqFillButton(
            stringsUtls.register, Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp), onClick = {
                signUpViewModel.onEvent(SignUpUIEvents.OnSignUpButtonClick)
            })


    }

    SystemBackButtonHandler {
        DrComposeAppRouter.navigateTo(Screens.Start)
    }


}


@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}


