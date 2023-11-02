package com.example.drCompose.koin

import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseManager {

    private
    val firebaseDatabase = FirebaseDatabase.getInstance()

    fun getFirebaseDatabase(): FirebaseDatabase {
        return firebaseDatabase
    }

}