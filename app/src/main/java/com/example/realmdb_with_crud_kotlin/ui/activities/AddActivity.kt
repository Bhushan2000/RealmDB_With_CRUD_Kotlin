package com.example.realmdb_with_crud_kotlin.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.realmdb_with_crud_kotlin.MyApplication
import com.example.realmdb_with_crud_kotlin.R
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.RealmViewModel
import com.example.realmdb_with_crud_kotlin.data.Repository.local.repo.RealmRepo
import com.example.realmdb_with_crud_kotlin.data.Repository.local.viewModel.RealmViewModelFactory
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.Todo
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.repo.TodoRepo
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.viewModel.TodoViewModelFactory
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.viewModel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.realm.kotlin.Realm
import org.mongodb.kbson.ObjectId

class AddActivity : AppCompatActivity() {
    private var _id: String = ""
    private var ID: Int = -1
    private var userID: Int = -1
    private var activity_code: Int = -1
    private var todo_title: String = ""
    private var todo_completed: Boolean = false

    private lateinit var editTodo: EditText
    private lateinit var addTodoButton: FloatingActionButton
    private lateinit var deleteTodoButton: FloatingActionButton
    private lateinit var addTodoOfflineButton: FloatingActionButton
    private lateinit var checkBox: CheckBox

    private lateinit var viewModel: TodoViewModel
    private lateinit var viewModel2: RealmViewModel

    private lateinit var realm: Realm
    private val TAG = "AddActivity"

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
        if (ID != -1) {
            editTodo.setText(todo_title)
            checkBox.isChecked = todo_completed
        }
    }

    fun initViewModel() {
        val repository = TodoRepo()
        val viewModelFactory = TodoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]

        realm = MyApplication.realm
        val repository2 = RealmRepo(realm)
        val viewModelFactory2 = RealmViewModelFactory(repository2)
        viewModel2 = ViewModelProvider(this, viewModelFactory2)[RealmViewModel::class.java]
    }

    private fun performOperations() {
        addTodoButton.setOnClickListener {
            if (ID == -1) {
                addTodo()
            } else if (activity_code == -3) {
                updateTodoOffline()
            } else {
                updateTodo()
            }
        }
        deleteTodoButton.setOnClickListener {
            if (activity_code == -3) {
                deleteTodoOffline()
            } else {
                deleteTodos()
            }
        }
        addTodoOfflineButton.setOnClickListener {
            addTodoOffline()
        }
    }

    private fun updateTodoOffline() {
        viewModel2.updateTodoText(editTodo.text.toString())
        viewModel2.updateTodoObjectId(_id)
        viewModel2.updateTodoCompleted(checkBox.isChecked)
        viewModel2.updateTodo()
        Snackbar.make(addTodoButton, "Todo updated in Offline mode..", Toast.LENGTH_SHORT).show()
        viewModel2.errorMessage.observe(this) {
            Snackbar.make(addTodoButton, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteTodoOffline() {
        val objectID = ObjectId(_id)
        viewModel2.deleteTodo(objectID)
        Snackbar.make(addTodoButton, "Todo delete from Offline mode..", Toast.LENGTH_SHORT).show()
        finish()
        viewModel2.errorMessage.observe(this) {
            Snackbar.make(addTodoButton, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addTodoOffline() {
        if (editTodo.text.toString().isEmpty()) {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                R.string.enter_todo_title,
                Toast.LENGTH_SHORT
            )
            snackbar.show()
        } else {
            val todo = Todo().apply {
                id = ID
                todo = editTodo.text.toString()
                completed = checkBox.isChecked
                userId = userID
            }
            viewModel2.addTodo(todo)
            Snackbar.make(addTodoButton, "Todo saved in Offline mode..", Toast.LENGTH_SHORT).show()
            viewModel.errorMessage.observe(this) {
                Snackbar.make(addTodoButton, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTodos() {

        viewModel.deleteTodo(ID).observe(this) { todo ->
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
            viewModel.updateTodo(ID, checkBox.isChecked).observe(this, Observer { todo ->
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
        ID = intent.getIntExtra("todo_id", -1)
        todo_title = intent.getStringExtra("todo_title").toString()
        todo_completed = intent.getBooleanExtra("todo_completed", false)
        userID = intent.getIntExtra("todo_user_id", -1)
        activity_code = intent.getIntExtra("activity_code", -1)
        if (activity_code == -3) {
            _id = intent.getStringExtra("_id").toString()
            Log.d(TAG, "getIntentData: _id $_id")
        }

        Log.d(TAG, "getIntentData: id $ID")
        Log.d(TAG, "getIntentData: todo_title $todo_title")
        Log.d(TAG, "getIntentData: completed $todo_completed")
        Log.d(TAG, "getIntentData: userID $userID")
        Log.d(TAG, "getIntentData: activity_code $activity_code")
    }

    fun initViews() {
        editTodo = findViewById(R.id.etEnterTodo)
        addTodoButton = findViewById(R.id.btn_add_todo)
        deleteTodoButton = findViewById(R.id.btn_delete_todo)
        addTodoOfflineButton = findViewById(R.id.floatingActionButton)
        checkBox = findViewById(R.id.check_is_completed)
        initViewModel()
        if (userID == -1) {
            deleteTodoButton.visibility = View.GONE
            addTodoOfflineButton.visibility = View.GONE
        } else if (activity_code == -3) {
            addTodoOfflineButton.visibility = View.GONE
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

// -2 for online
// -3 for offline
