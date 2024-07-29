package com.example.realmdb_with_crud_kotlin.data.Repository.remote.retrofit

import com.example.realmdb_with_crud_kotlin.data.models.BaseModel
import com.example.realmdb_with_crud_kotlin.data.models.DeleteModelResponse
import com.example.realmdb_with_crud_kotlin.data.models.Todos
import com.example.realmdb_with_crud_kotlin.data.models.AddModel
import com.example.realmdb_with_crud_kotlin.data.models.UpdateModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface API {
    // Get todo
    @GET("todos")
    suspend fun getTodos(): BaseModel

    // update todo
    @Headers("Accept: application/json")
    @PUT("todos/{id}")
    suspend fun updateTodos(@Path("id") id: Int, @Body todo: UpdateModel): Todos

    // delete todo
    @DELETE("todos/{id}")
    suspend fun deleteTodos(@Path("id") id: Int): DeleteModelResponse

    // add todo
    @Headers("Accept: application/json")
    @POST("todos/add")
    suspend fun addTodos(@Body todo: AddModel): Todos
}