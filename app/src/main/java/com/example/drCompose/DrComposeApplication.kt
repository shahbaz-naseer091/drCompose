package com.example.drCompose

import android.app.Application
import com.example.drCompose.koin.firebaseModule
import com.example.drCompose.koin.loginViewModelModule
import com.example.drCompose.koin.preferenceModule
import com.example.drCompose.koin.signUpViewModelModule
import com.example.drCompose.koin.stringUtilsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DrComposeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DrComposeApplication)
            modules(
                preferenceModule,
                stringUtilsModule,
                loginViewModelModule,
                signUpViewModelModule,
                firebaseModule
            )
        }
    }

}