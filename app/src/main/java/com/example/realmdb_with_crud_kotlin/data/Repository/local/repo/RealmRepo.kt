package com.example.realmdb_with_crud_kotlin.data.Repository.local.repo

import android.util.Log
import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.Todo
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class RealmRepo(val realm: Realm) : RealmInterface {

    private val TAG = "MongoRepo"

    override suspend fun getData(): Flow<List<Todo>> {
        return realm.query(Todo::class).asFlow().map { it.list }
    }

    override suspend fun addData(todo: Todo) {
        realm.write { copyToRealm(todo) }
    }
    override suspend fun updateData(todo: Todo) {
        realm.write {
            val queriedTodo = query<Todo>(query = "_id == $0", todo._id).first().find()
            queriedTodo?.todo = todo.todo
            queriedTodo?.completed = todo.completed
            Log.d(TAG, "updateData: ${queriedTodo?.todo}")
        }
    }

    override suspend fun deleteData(id: ObjectId) {
        realm.write {
            val todo = query<Todo>(query = "_id == $0", id).first().find()
            try {
                todo?.let { delete(it) }
            } catch (e: Exception) {
                Log.d(TAG, ": deleteData: ${e.message}")
            }
        }
    }
}