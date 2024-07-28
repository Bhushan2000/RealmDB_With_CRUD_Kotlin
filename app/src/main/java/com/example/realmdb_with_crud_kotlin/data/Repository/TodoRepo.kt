package com.example.realmdb_with_crud_kotlin.data.Repository

import com.example.realmdb_with_crud_kotlin.data.Repository.remote.RetrofitInstance
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import com.example.realmdb_with_crud_kotlin.data.models.send.AddModel
import com.example.realmdb_with_crud_kotlin.data.models.send.UpdateModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class TodoRepo {
    // Establish a repository class to handle data fetching logic. This isolates the data source from your ViewModels.
    private val apiService = RetrofitInstance.apiService
    suspend fun getTodos() = apiService.getTodos()
    suspend fun addTodo(todo: AddModel) = apiService.addTodos(todo)
    suspend fun updateTodo(id: Int, todo: UpdateModel) = apiService.updateTodos(id, todo)
    suspend fun deleteTodo(id: Int) = apiService.deleteTodos(id)

    // local db
    val config = RealmConfiguration.Builder(schema = setOf(Todo::class))
        .name("myrealm.realm")
        .schemaVersion(1)
        .deleteRealmIfMigrationNeeded()
        .build()
}