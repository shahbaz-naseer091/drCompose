package com.example.drCompose.data.signUp

import android.content.ContentResolver
import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.drCompose.data.models.Users
import com.example.drCompose.koin.FirebaseDatabaseManager
import com.example.drCompose.koin.PreferenceManager
import com.example.drCompose.koin.StringsUtls
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.drCompose.screens.signUpCoroutineScope
import com.example.drCompose.utils.Constants.USERS
import com.example.drCompose.utils.Extensions.base64
import com.example.drCompose.utils.Extensions.uriToBitmap
import com.example.drCompose.validator.Validator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignUpViewModel(
    private val firebaseManager: FirebaseDatabaseManager,
    val prefManager: PreferenceManager,
    val stringsUtils: StringsUtls
) : ViewModel() {

    val TAG = "SignUpViewModel"

    var contentResolver: ContentResolver? = null

    var signUpUIState = mutableStateOf(SignUpUIState())

    val snackbarHostState = mutableStateOf(SnackbarHostState())

    var errorMessage = mutableStateOf("")

    var signUpInProgress = mutableStateOf(false)

    var isGenderDialogOpen = mutableStateOf(false)
    var isDatePickerOpen = mutableStateOf(false)
    var isAttachmentDialogOpen = mutableStateOf(false)

    var allValidationPassed = mutableStateOf(false)


    fun onEvent(event: SignUpUIEvents) {

        when (event) {

            is SignUpUIEvents.OnSelectProfilePhoto -> {
                signUpUIState.value = signUpUIState.value.copy(
                    profilePhoto = event.uri
                )
                signUpUIState.value.profilePhotoBase64 =
                    event.uri.uriToBitmap(contentResolver!!)?.base64()
            }

            is SignUpUIEvents.OnFirstNameChange -> {
                signUpUIState.value = signUpUIState.value.copy(
                    firstName = event.firstName
                )
            }

            is SignUpUIEvents.OnMiddleNameChange -> {
                signUpUIState.value = signUpUIState.value.copy(
                    middleName = event.middleName
                )
            }

            is SignUpUIEvents.OnLastNameChange -> {
                signUpUIState.value = signUpUIState.value.copy(
                    lastName = event.lastName
                )
            }

            is SignUpUIEvents.OnEmailChange -> {
                signUpUIState.value = signUpUIState.value.copy(
                    email = event.email
                )
            }

            is SignUpUIEvents.OnPasswordChange -> {
                signUpUIState.value = signUpUIState.value.copy(
                    password = event.password
                )
            }

            is SignUpUIEvents.OnDateSelection -> {
                signUpUIState.value = signUpUIState.value.copy(
                    date = event.date
                )
            }

            is SignUpUIEvents.OnGenderSelection -> {
                signUpUIState.value = signUpUIState.value.copy(
                    gender = event.gender
                )
            }

            else -> {

                validateSignUpUIDataWithRules()

                if (allValidationPassed.value)
                    signUp()
                else {
                    signUpCoroutineScope?.launch {
                        showSnackbar(errorMessage.value)
                    }
                }

            }

        }

    }

    private fun validateSignUpUIDataWithRules() {

        signUpUIState.value.apply {
            firstNameError = false
            middleNameError = false
            lastNameError = false
            emailError = false
            dateError = false
            genderError = false
        }

        val emailResult = Validator.validateEmail(signUpUIState.value.email)
        val passwordResult = Validator.validateEmail(signUpUIState.value.password)

        signUpUIState.value.apply {


            if (firstName.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseEnterFirstName

                signUpUIState.value = signUpUIState.value.copy(
                    firstNameError = true
                )

            }

            //middle name (optional)
            /*else if (middleName.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseEnterMiddleName

                signUpUIState.value = signUpUIState.value.copy(
                    middleNameError = true
                )

            }*/
            else if (lastName.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseEnterLastName
                signUpUIState.value = signUpUIState.value.copy(
                    lastNameError = true
                )

            } else if (email.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseEnterEmail
                signUpUIState.value = signUpUIState.value.copy(
                    emailError = true
                )

            } else if (emailResult.status.not()) {

                errorMessage.value = emailResult.message
                signUpUIState.value = signUpUIState.value.copy(
                    emailError = true
                )

            } else if (password.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseEnterPassword
                signUpUIState.value = signUpUIState.value.copy(
                    passwordError = true
                )

            } else if (passwordResult.status.not()) {

                errorMessage.value = emailResult.message
                signUpUIState.value = signUpUIState.value.copy(
                    passwordError = true
                )

            } else if (date.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseSelectDOB
                signUpUIState.value = signUpUIState.value.copy(
                    dateError = true
                )

            } else if (gender.isEmpty()) {

                errorMessage.value = stringsUtils.errorPleaseSelectGender
                signUpUIState.value = signUpUIState.value.copy(
                    genderError = true
                )

            } else {
                errorMessage.value = ""
            }

            allValidationPassed.value = firstNameError.not() && lastNameError.not()
                    && emailError.not() && emailResult.status && dateError.not() && genderError.not()

        }


    }

    private fun signUp() = CoroutineScope(Dispatchers.IO).launch {

        signUpInProgress.value = true

        val user = Users().apply {
            firstName = signUpUIState.value.firstName
            middleName = signUpUIState.value.middleName
            lastName = signUpUIState.value.lastName
            email = signUpUIState.value.email
            password = signUpUIState.value.password
            birthDate = signUpUIState.value.date
            gender = signUpUIState.value.gender

            signUpUIState.value.profilePhoto?.let {
                profilePicture = signUpUIState.value.profilePhotoBase64 ?: ""
            }
        }

        val databaseReference = firebaseManager.getFirebaseDatabase().reference.child(USERS).push()

        withContext(Dispatchers.IO) {

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    databaseReference.setValue(user)
                    signUpUIState.value = SignUpUIState()

                    signUpInProgress.value = false
                    DrComposeAppRouter.navigateTo(Screens.Start)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                    signUpInProgress.value = false
                }

            })
        }

    }

}


suspend fun SignUpViewModel.showSnackbar(message: String) {
    snackbarHostState.value.showSnackbar(message, "Dismiss")
}