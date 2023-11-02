package com.example.drCompose.koin

import android.content.Context
import com.example.driqpro.R

class StringsUtls(context: Context) {

    val selectImageFrom: String = context.strings(R.string.select_image_from)
    val homeScreen: String = context.strings(R.string.home_screen)
    val register: String = context.strings(R.string.register)
    val welcomeToDrIQPleaseSignIn = context.strings(R.string.welcome_to_dr_iq_please_sign_in)
    val forgotEmailAddressOrPassword = context.strings(R.string.forgot_email_address_or_password)
    val typeYourEmailAddress = context.strings(R.string.type_your_email_address)
    val typeYourPassword = context.strings(R.string.type_your_password)
    val titleActivitySignUp = context.strings(R.string.title_activity_sign_up)
    val createAccount = context.strings(R.string.create_account)
    val firstName = context.strings(R.string.first_name)
    val typeFirstNameHere = context.strings(R.string.type_first_name_here)
    val middleName = context.strings(R.string.middle_name)
    val typeMiddleNameHere = context.strings(R.string.type_middle_name_here)
    val lastName = context.strings(R.string.last_name)
    val dateOfBirth = context.strings(R.string.date_of_birth)
    val selectDateOfBirth = context.strings(R.string.select_date_of_birth)
    val addProfilePhoto = context.strings(R.string.add_profile_photo)
    val typeLastNameHere = context.strings(R.string.type_last_name_here)
    val gender = context.strings(R.string.gender)
    val selectGender = context.strings(R.string.select_gender)
    val email = context.strings(R.string.email)
    val password = context.strings(R.string.password)
    val signIn = context.strings(R.string.signin)

    val errorPleaseEnterFirstName: String = context.strings(R.string.error_please_enter_first_name)
    val errorPleaseEnterMiddleName: String = context.strings(R.string.error_please_enter_middle_name)
    val errorPleaseEnterLastName: String = context.strings(R.string.error_please_enter_last_name)
    val errorPleaseEnterEmail: String = context.strings(R.string.error_please_enter_email)
    val errorPleaseEnterPassword: String = context.strings(R.string.error_please_enter_password)
    val errorPleaseSelectDOB: String = context.strings(R.string.error_please_select_dob)
    val errorPleaseSelectGender: String = context.strings(R.string.error_please_select_gender)



}

fun Context.strings(id: Int): String {
    return resources.getString(id)
}