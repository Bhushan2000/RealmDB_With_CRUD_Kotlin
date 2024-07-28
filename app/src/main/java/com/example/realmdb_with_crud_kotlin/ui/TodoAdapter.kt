package com.example.realmdb_with_crud_kotlin.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realmdb_with_crud_kotlin.R
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import com.example.realmdb_with_crud_kotlin.data.viewModel.TodoViewModel

class TodoAdapter(
    private var todoList: MutableList<Todos>,
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var todoTxt = itemView.findViewById<android.widget.TextView>(R.id.txtTodo)
        var checkBox = itemView.findViewById<android.widget.CheckBox>(R.id.checkBox)
        var txtUserID = itemView.findViewById<android.widget.TextView>(R.id.txtUserID)
        var todoNo = itemView.findViewById<android.widget.TextView>(R.id.txtTodoID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        holder.txtUserID.text = "User ID : ${todo.userId.toString()}"
        holder.todoNo.text = todo.id.toString()
        holder.todoTxt.text = todo.todo
        holder.checkBox.isChecked = todo.completed == true
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddActivity::class.java).apply {
                putExtra("todo_id", todo.id)
                putExtra("todo_title", todo.todo)
                putExtra("todo_completed", todo.completed)
                putExtra("todo_user_id", todo.userId)
                putExtra("button_title", R.string.update_todo.toString())
            }
            it.context.startActivity(intent)
        }
    }

    fun deleteItem(position: Int) {
        todoList.removeAt(position)
        notifyDataSetChanged()
    }
}