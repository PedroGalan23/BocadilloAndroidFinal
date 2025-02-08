package com.example.bocadilloandroidfinal

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)  // Asegura que Firebase se inicializa correctamente
    }
}
