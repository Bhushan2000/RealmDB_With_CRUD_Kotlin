package com.example.realmdb_with_crud_kotlin.data.Repository.local.repo

import com.example.realmdb_with_crud_kotlin.data.Repository.local.model.Todo
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface RealmInterface {
    suspend fun getData(): Flow<List<Todo>>
    suspend fun addData(todo: Todo)
    suspend fun updateData(todo: Todo)
    suspend fun deleteData(id: ObjectId)
}