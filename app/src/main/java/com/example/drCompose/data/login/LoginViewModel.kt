package com.example.drCompose.data.login

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.drCompose.data.models.Users
import com.example.drCompose.koin.FirebaseDatabaseManager
import com.example.drCompose.koin.PreferenceManager
import com.example.drCompose.navigation.DrComposeAppRouter
import com.example.drCompose.navigation.Screens
import com.example.drCompose.utils.Constants.USERS
import com.example.drCompose.validator.Validator.validateEmail
import com.example.drCompose.validator.Validator.validatePassword
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginViewModel(val prefManger: PreferenceManager, private val firebaseManager : FirebaseDatabaseManager) : ViewModel() {

    var loginCoroutineScope: CoroutineScope? = null

    var loginUiState = mutableStateOf(LoginUIState())

    var allValidationPassed = mutableStateOf(false)

    var errorMessage = mutableStateOf("")

    val snackbarHostState = mutableStateOf(SnackbarHostState())

    var loginInProgress = mutableStateOf(false)

    init {
        validateLoginUIDataWithRules()
    }

    fun onEvent(event: LoginUIEvents) {

        when (event) {

            is LoginUIEvents.OnEmailChanage -> {
                loginUiState.value = loginUiState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvents.OnPasswordChanage -> {
                loginUiState.value = loginUiState.value.copy(
                    password = event.password
                )

            }

            else -> {
                login()
            }

        }

        validateLoginUIDataWithRules()

    }

    private fun login() {

        validateLoginUIDataWithRules()

        if (allValidationPassed.value) {

            loginInProgress.value = true


            val usersRef = firebaseManager.getFirebaseDatabase().getReference(USERS)

            val emailToCheck = loginUiState.value.email
            val passwordToCheck = loginUiState.value.password

            val query = usersRef.orderByChild("email").equalTo(emailToCheck)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (user in snapshot.children) {
                            val userObj = user.getValue(Users::class.java)
                            if (userObj?.password == passwordToCheck) {
                                loginInProgress = mutableStateOf(false)

                                loginUiState.value.apply {
                                    prefManger.saveEmail(this.email)
                                    prefManger.savePassword(this.password)
                                    prefManger.saveIsLoggedIn(true)
                                }

                                DrComposeAppRouter.navigateTo(Screens.Home)

                            } else {
                                loginCoroutineScope?.launch {
                                    showSnackbar("Password is incorrect")
                                    loginInProgress.value = false
                                }
                            }
                        }
                    } else {
                        loginInProgress.value = false
                        loginCoroutineScope?.launch {
                            showSnackbar("User does not exist")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    loginInProgress.value = false
                    loginCoroutineScope?.launch {
                        showSnackbar("Something went wrong")
                    }
                }
            })

        }

    }

    private fun validateLoginUIDataWithRules() {

        val emailResult = validateEmail(loginUiState.value.email)

        val passwordResult = validatePassword(loginUiState.value.password)

        errorMessage.value =
            if (emailResult.status.not()) emailResult.message else if (passwordResult.status.not()) passwordResult.message else ""

        loginUiState.value = loginUiState.value.copy(
            emailError = emailResult.status.not(),
            passwordError = passwordResult.status.not()
        )

        allValidationPassed.value = emailResult.status && passwordResult.status

    }


}

suspend fun LoginViewModel.showSnackbar(message: String) {
    snackbarHostState.value.showSnackbar(message, "Dismiss")
}
