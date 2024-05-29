package com.ahmedapps.bankningappui

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginFlowApp : Application (){
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

    }


}