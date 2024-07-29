package com.example.realmdb_with_crud_kotlin.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.realmdb_with_crud_kotlin.R
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.repo.TodoRepo
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.viewModel.TodoViewModelFactory
import com.example.realmdb_with_crud_kotlin.data.Repository.remote.viewModel.TodoViewModel
import com.example.realmdb_with_crud_kotlin.ui.adapters.TodoAdapter
import com.example.realmdb_with_crud_kotlin.utils.NetworkUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var add_fab: FloatingActionButton
    private lateinit var offline_fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var rel_layout: RelativeLayout
    private lateinit var textView_offline: TextView
    private lateinit var imageView_wifi: ImageView
    private lateinit var adapter: TodoAdapter
    private lateinit var viewModel: TodoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setInsets()
        initViews()
        checkNetwork()
        addTodo()
        getAllTodos()
        offline_realmDB()
        Log.d(MainActivity::class.java.simpleName, "Network,......: ${NetworkUtil.isNetworkAvailable(this)}")

    }

    private fun checkNetwork() {
        if ( NetworkUtil.isNetworkAvailable(this)) {
            textView_offline.visibility = View.GONE
            imageView_wifi.visibility = View.GONE
        } else {
            add_fab.visibility = View.GONE
        }
    }

    private fun offline_realmDB() {
        offline_fab.setOnClickListener {
            val intent = Intent(this, OfflineActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllTodos() {
        // Observe LiveData and update UI
        viewModel.fetchTodo().observe(this) { todo ->
            // Update your UI with the fetched user data
            // For example, populate a RecyclerView
            try {
                rel_layout.visibility = View.GONE

                if (todo.todos.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "No data found", Toast.LENGTH_SHORT).show()
                } else {
                    setupRecyclerView(todo.todos)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewModel.errorMessage.observe(this) {
            Snackbar.make(recyclerView, it, Toast.LENGTH_SHORT).show()
            rel_layout.visibility = View.GONE
        }
    }

    fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun initViews() {
        add_fab = findViewById<FloatingActionButton>(R.id.add_fab)
        offline_fab = findViewById<FloatingActionButton>(R.id.offline_fab)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        rel_layout = findViewById(R.id.rel_layout)
        textView_offline = findViewById(R.id.textView_offline)
        imageView_wifi = findViewById(R.id.imageView_wifi)
        initViewModel()
    }

    fun initViewModel() {
        val repository = TodoRepo()
        val viewModelFactory = TodoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]
    }

    fun addTodo() {
        add_fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    fun setupRecyclerView(todoList: MutableList<Todos>) {
        adapter = TodoAdapter(todoList)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
    }
}

// Key Points:
//ViewModelScope: Use viewModelScope.launch to run coroutines within the ViewModel's lifecycle.
//Error Handling: Implement proper error handling in your ViewModel to gracefully handle API failures.
//UI Updates: Update your UI elements within the observe block on the main thread.
//Additional Considerations:
//Dependency Injection: Consider using a dependency injection framework like Hilt to manage dependencies more effectively.
//Caching: If applicable, implement caching in your repository to reduce network requests.
//State Management: For more complex UI states, explore using a state management library like Flow or StateFlow

