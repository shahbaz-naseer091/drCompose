package com.example.drCompose.koin

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager (context: Context) {

    companion object{

        const val PREFERENCE_NAME = "drCompose"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val IS_LOGGED_IN = "is_logged_in"

    }

    val preference : SharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }


    fun saveEmail(email: String){
        preference.edit().putString(EMAIL, email).apply()
    }

    fun savePassword(password: String){
        preference.edit().putString(PASSWORD, password).apply()
    }

    fun saveIsLoggedIn(isLoggedIn: Boolean){
        preference.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun getEmail(): String?{
        return preference.getString(EMAIL, "")
    }

    fun getPassword(): String?{
        return preference.getString(PASSWORD, "")
    }

    fun getIsLoggedIn(): Boolean{
        return preference.getBoolean(IS_LOGGED_IN, false)
    }


}