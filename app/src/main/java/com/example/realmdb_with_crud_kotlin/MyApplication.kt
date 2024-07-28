package com.example.realmdb_with_crud_kotlin

import android.app.Application
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Realm. Should only be done once when the application starts.
    }
}