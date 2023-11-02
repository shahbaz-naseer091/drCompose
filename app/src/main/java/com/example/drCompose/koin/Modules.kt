package com.example.drCompose.koin

import com.example.drCompose.data.login.LoginViewModel
import com.example.drCompose.data.signUp.SignUpViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val preferenceModule = module {
    single { PreferenceManager(context = get()) }
}

val stringUtilsModule = module {
    single { StringsUtls(context = get()) }
}

val loginViewModelModule = module {
    single { LoginViewModel(get(),get()) }
}

val signUpViewModelModule = module {
    single { SignUpViewModel(get(),get(),get()) }
}

val firebaseModule = module {
    single { FirebaseDatabaseManager() }
}