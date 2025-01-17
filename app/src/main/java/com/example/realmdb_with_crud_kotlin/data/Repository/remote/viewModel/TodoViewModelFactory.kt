package com.example.realmdb_with_crud_kotlin.data.Repository.remote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.repo.TodoRepo

class TodoViewModelFactory(private val repo: TodoRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}