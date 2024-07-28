package com.example.realmdb_with_crud_kotlin.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.realmdb_with_crud_kotlin.R
import com.example.realmdb_with_crud_kotlin.data.Repository.TodoRepo
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.RetrofitInstance
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import com.example.realmdb_with_crud_kotlin.data.viewModel.MyViewModelFactory
import com.example.realmdb_with_crud_kotlin.data.viewModel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AddActivity : AppCompatActivity() {
    private var id: Int = -1
    private var userID: Int = -1
    private lateinit var todo_title: String
    private lateinit var button_title: String
    private var completed: Boolean = false
    private lateinit var editTodo: EditText
    private lateinit var addTodoButton: FloatingActionButton
    private lateinit var deleteTodoButton: FloatingActionButton
    private lateinit var checkBox: CheckBox
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        getIntentData()
        setupInsets()
        initViews()
        setDataOnViews()
        performOperations()
    }

    private fun setDataOnViews() {
        if (id != -1) {
            editTodo.setText(todo_title)
            checkBox.isChecked = completed
        }
    }

    fun initViewModel() {
        val repository = TodoRepo()
        val viewModelFactory = MyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]
    }

    private fun performOperations() {
        addTodoButton.setOnClickListener {
            if (id == -1) {
                addTodo()
            } else {
                updateTodo()
            }
        }
        deleteTodoButton.setOnClickListener {
            deleteTodos()
        }
    }

    private fun deleteTodos() {

        viewModel.deleteTodo(id).observe(this) { todo ->
            // handle todo
            if (todo != null) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    todo.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                // reset views
                checkBox.isChecked = false
                editTodo.setText("")
                finish()
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.errorMessage.observe(this) {
            Snackbar.make(addTodoButton, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addTodo() {
        if (editTodo.text.toString().isEmpty()) {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                R.string.enter_todo_title,
                Toast.LENGTH_SHORT
            )
            snackbar.show()
        } else {
            add()
        }
    }

    private fun add() {
        try {
            viewModel.addTodo(editTodo.text.toString()).observe(this, Observer { todo ->
                // handle todo
                if (todo != null) {
                    Snackbar.make(addTodoButton, todo.toString(), Toast.LENGTH_SHORT).show()
                    // reset views
                    checkBox.isChecked = false
                    editTodo.setText("")
                    finish()
                } else {
                    Snackbar.make(addTodoButton, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewModel.errorMessage.observe(this) {
            Snackbar.make(addTodoButton, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTodo() {
        try {
            viewModel.updateTodo(id, checkBox.isChecked).observe(this, Observer { todo ->
                if (todo != null) {
                    // Update UI with the user response
                    Snackbar.make(addTodoButton, todo.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    // Handle the error case
                    Snackbar.make(addTodoButton, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

        viewModel.errorMessage.observe(this) {
            Snackbar.make(addTodoButton, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getIntentData() {
        id = intent.getIntExtra("todo_id", -1)
        todo_title = intent.getStringExtra("todo_title").toString()
        completed = intent.getBooleanExtra("todo_completed", false)
        userID = intent.getIntExtra("todo_user_id", -1)
    }

    fun initViews() {
        editTodo = findViewById(R.id.etEnterTodo)
        addTodoButton = findViewById(R.id.btn_add_todo)
        deleteTodoButton = findViewById(R.id.btn_delete_todo)
        checkBox = findViewById(R.id.check_is_completed)
        initViewModel()
        if (userID == -1) {
            deleteTodoButton.visibility = View.GONE
        }
    }

    fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}