package com.example.realmdb_with_crud_kotlin

import android.app.Application
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.Todo
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


class MyApplication : Application() {
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize Realm. Should only be done once when the application starts.
        val config = RealmConfiguration.create(schema = setOf(Todo::class))
        realm = Realm.open(config)
    }
}