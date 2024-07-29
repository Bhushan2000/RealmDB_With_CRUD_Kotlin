package com.example.realmdb_with_crud_kotlin.data.models

import com.google.gson.annotations.SerializedName

data class DeleteModelResponse(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("todo")
    var todo: String? = null,
    @SerializedName("completed")
    var completed: Boolean? = null,
    @SerializedName("userId")
    var userId: Int? = null,
    @SerializedName("isDeleted")
    var isDeleted: Boolean? = null,
    @SerializedName("deletedOn")
    var deletedOn: String? = null
)