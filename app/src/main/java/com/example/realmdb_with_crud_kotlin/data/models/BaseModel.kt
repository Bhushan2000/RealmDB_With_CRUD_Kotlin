package com.example.realmdb_with_crud_kotlin.data.models

import com.google.gson.annotations.SerializedName

data class BaseModel(
    @SerializedName("todos") var todos: ArrayList<Todos> = arrayListOf(),
    @SerializedName("total") var total: Int? = null,
    @SerializedName("skip") var skip: Int? = null,
    @SerializedName("limit") var limit: Int? = null
)