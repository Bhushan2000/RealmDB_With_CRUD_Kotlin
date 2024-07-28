package com.example.realmdb_with_crud_kotlin.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmdb_with_crud_kotlin.data.Repository.TodoRepo
import com.example.realmdb_with_crud_kotlin.data.models.BaseModel
import com.example.realmdb_with_crud_kotlin.data.models.receive.DeleteModelResponse
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import com.example.realmdb_with_crud_kotlin.data.models.send.AddModel
import com.example.realmdb_with_crud_kotlin.data.models.send.UpdateModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class TodoViewModel(private val repository: TodoRepo) : ViewModel() {
    // Create a ViewModel that interacts with the repository and exposes LiveData to your Activity.
    // If your ViewModel needs dependencies (like a repository), use a ViewModelProvider.Factory to provide them:

    private val _todos = MutableLiveData<BaseModel>()
    val todos: LiveData<BaseModel> get() = _todos

    private val _todo = MutableLiveData<Todos>()
    val todo: LiveData<Todos> get() = _todo

    private val _delete = MutableLiveData<DeleteModelResponse>()
    val delete: LiveData<DeleteModelResponse> get() = _delete

    val errorMessage = MutableLiveData<String>()

    init {
        fetchTodo()
    }

    fun fetchTodo():LiveData<BaseModel> {
        viewModelScope.launch {
            try {
                _todos.value = repository.getTodos()
            } catch (e: IOException) {
                // Network error (no internet connection, timeout, etc.)
                errorMessage.value = "Network Error: ${e.message}"
            } catch (e: HttpException) {
                // HTTP error (invalid response, 4xx/5xx status code)
                errorMessage.value = "HTTP Error: ${e.message}"
            } catch (e: Exception) {
                // General error (unexpected exceptions)
                errorMessage.value = "Unexpected Error: ${e.message}"
            }
        }
        return todos
    }

    fun addTodo(todo_title: String): LiveData<Todos> {
        viewModelScope.launch {
            try {
                val todo = AddModel(
                    completed = false,
                    todo = todo_title,
                    userId = 1,
                )
                _todo.value = repository.addTodo(todo)

            } catch (e: IOException) {
                // Network error (no internet connection, timeout, etc.)
                errorMessage.value = "Network Error: ${e.message}"
            } catch (e: HttpException) {
                // HTTP error (invalid response, 4xx/5xx status code)
                errorMessage.value = "HTTP Error: ${e.message}"
            } catch (e: Exception) {
                // General error (unexpected exceptions)
                errorMessage.value = "Unexpected Error: ${e.message}"
            }
        }
        return todo
    }

    fun updateTodo(id: Int, completed: Boolean): LiveData<Todos> {
        viewModelScope.launch {
            try {
                val todo = UpdateModel(
                    completed = completed
                )
                _todo.value = repository.updateTodo(id, todo)

            } catch (e: IOException) {
                // Network error (no internet connection, timeout, etc.)
                errorMessage.value = "Network Error: ${e.message}"
            } catch (e: HttpException) {
                // HTTP error (invalid response, 4xx/5xx status code)
                errorMessage.value = "HTTP Error: ${e.message}"
            } catch (e: Exception) {
                // General error (unexpected exceptions)
                errorMessage.value = "Unexpected Error: ${e.message}"
            }
        }
        return todo
    }

    fun deleteTodo(id: Int):LiveData<DeleteModelResponse> {
        viewModelScope.launch {
            try {
                _delete.value = repository.deleteTodo(id)
            } catch (e: IOException) {
                // Network error (no internet connection, timeout, etc.)
                errorMessage.value = "Network Error: ${e.message}"
            } catch (e: HttpException) {
                // HTTP error (invalid response, 4xx/5xx status code)
                errorMessage.value = "HTTP Error: ${e.message}"
            } catch (e: Exception) {
                // General error (unexpected exceptions)
                errorMessage.value = "Unexpected Error: ${e.message}"
            }
        }
        return delete
    }
}