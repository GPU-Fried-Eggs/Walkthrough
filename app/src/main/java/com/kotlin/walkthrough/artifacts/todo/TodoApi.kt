package com.kotlin.walkthrough.artifacts.todo

import com.kotlin.walkthrough.artifacts.todo.model.Todo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://localhost:8686"

interface TodoApi {
    @GET("/todos")
    suspend fun getTodos(): List<Todo>

    companion object {
        var todoService: TodoApi? = null

        fun getInstance(): TodoApi {
            if (todoService === null)
                todoService = createAdapter()?.build()?.create(TodoApi::class.java)
            return todoService!!
        }

        private fun createAdapter(): Retrofit.Builder? {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory
                        .create()
                )
        }
    }
}