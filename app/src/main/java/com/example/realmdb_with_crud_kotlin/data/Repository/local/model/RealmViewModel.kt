package com.example.realmdb_with_crud_kotlin.data.Repository.local.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmdb_with_crud_kotlin.data.Repository.local.repo.RealmRepo
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import retrofit2.HttpException
import java.io.IOException

class RealmViewModel(private val repository: RealmRepo) : ViewModel() {

    private val _todos = MutableLiveData<MutableList<Todo>>()
    val todos: LiveData<MutableList<Todo>> get() = _todos

    private val _todoL = MutableLiveData<Todo>()
    val todoL: LiveData<Todo> get() = _todoL

    val errorMessage = MutableLiveData<String>()

    private var todo_text = ""
    private var objectId = ""
    private var todo_completed = false

    init {
        fetchTodos()
    }

    fun updateTodoText(name: String) {
        this.todo_text = name
    }

    fun updateTodoObjectId(id: String) {
        this.objectId = id
    }

    fun updateTodoCompleted(completed: Boolean) {
        this.todo_completed = completed
    }

    fun fetchTodos(): LiveData<MutableList<Todo>> {
        viewModelScope.launch {
            try {
                repository.getData()
                    .collect { todos ->
                        _todos.value = todos.toMutableList()
                    }
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

    fun addTodo(todo: Todo): LiveData<Todo> {
        viewModelScope.launch {
            try {
                repository.addData(todo)
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
        return todoL
    }

    fun updateTodo() {
        viewModelScope.launch {
            try {
                repository.updateData(Todo().apply {
                    _id = ObjectId(hexString = objectId)
                    todo = todo_text
                    completed = todo_completed
                })

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
    }

    fun deleteTodo(id: ObjectId) {
        viewModelScope.launch {
            try {
                repository.deleteData(id)
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
    }
}