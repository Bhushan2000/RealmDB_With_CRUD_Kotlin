package com.example.realmdb_with_crud_kotlin.data.Repository.remote.repo

import com.example.realmdb_with_crud_kotlin.data.Repository.remote.retrofit.RetrofitInstance
import com.example.realmdb_with_crud_kotlin.data.models.AddModel
import com.example.realmdb_with_crud_kotlin.data.models.UpdateModel

class TodoRepo {
    // Establish a repository class to handle data fetching logic. This isolates the data source from your ViewModels.
    private val apiService = RetrofitInstance.apiService
    suspend fun getTodos() = apiService.getTodos()
    suspend fun addTodo(todo: AddModel) = apiService.addTodos(todo)
    suspend fun updateTodo(id: Int, todo: UpdateModel) = apiService.updateTodos(id, todo)
    suspend fun deleteTodo(id: Int) = apiService.deleteTodos(id)
}