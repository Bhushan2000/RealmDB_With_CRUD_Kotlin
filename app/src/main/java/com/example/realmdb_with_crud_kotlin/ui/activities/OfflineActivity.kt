package com.example.realmdb_with_crud_kotlin.ui.activities

import android.content.Intent
import android.os.Bundle
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
import com.example.realmdb_with_crud_kotlin.MyApplication
import com.example.realmdb_with_crud_kotlin.R
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.RealmViewModel
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.Todo
import com.example.realmdb_with_crud_kotlin.data.Repository.local.repo.RealmRepo
import com.example.realmdb_with_crud_kotlin.data.Repository.local.viewModel.RealmViewModelFactory
import com.example.realmdb_with_crud_kotlin.ui.adapters.TodoOfflineAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.realm.kotlin.Realm

class OfflineActivity : AppCompatActivity() {
    private lateinit var add_fab: FloatingActionButton
    private lateinit var offline_fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var rel_layout: RelativeLayout
    private lateinit var textView_offline: TextView
    private lateinit var imageView_wifi: ImageView
    private lateinit var adapter: TodoOfflineAdapter
    private lateinit var viewModel: RealmViewModel
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setInsets()
        initViews()
        getAllTodos()
        performOperations()

        // Add your offline logic
    }


    fun initViewModel() {
        realm = MyApplication.realm
        val repository = RealmRepo(realm)
        val viewModelFactory = RealmViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RealmViewModel::class.java]
    }

    private fun getAllTodos() {
        // Observe LiveData and update UI
        viewModel.fetchTodos().observe(this) { todo ->
            // Update your UI with the fetched user data
            // For example, populate a RecyclerView
            try {
                rel_layout.visibility = View.GONE

                if (todo.isEmpty()) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "No data found",
                        Toast.LENGTH_SHORT
                    ).show()
                    textView_offline.visibility = View.VISIBLE
                    textView_offline.text = "No data found"
                } else {
                    textView_offline.visibility = View.GONE
                    setupRecyclerView(todo)
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

    fun setupRecyclerView(todoList: MutableList<Todo>) {
        adapter = TodoOfflineAdapter(todoList)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
    }

    private fun performOperations() {
        add_fab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
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
        textView_offline.visibility = View.GONE
        imageView_wifi.visibility = View.GONE
        offline_fab.visibility = View.GONE
        add_fab.visibility = View.GONE
        initViewModel()
    }
}