package com.example.realmdb_with_crud_kotlin.data.models

data class AddModel(
    val completed: Boolean,
    val todo: String,
    val userId: Int
)