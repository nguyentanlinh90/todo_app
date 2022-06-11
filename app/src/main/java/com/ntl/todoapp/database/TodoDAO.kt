package com.ntl.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ntl.todoapp.model.Todo

@Dao
interface TodoDAO {

    @Insert
    fun insertTodo(todo: Todo?)
    @Insert
    suspend fun insertTodoS(todo: Todo?)

    @Query("SELECT * FROM todo")
    fun getAllTodo(): List<Todo>?

    @Query("SELECT * FROM todo")
    fun getAllTodoLive(): LiveData<List<Todo>>?

    @Query("SELECT * FROM todo where name= :name")
    fun getTodoByName(name: String?): Todo?

    @Update
    fun updateTodo(todo: Todo?)

    @Delete
    fun deleteTodo(todo: Todo?)

    @Delete
    suspend fun deleteTodoS(todo: Todo?)

    @Query("DELETE FROM todo")
    fun deleteAllTodo()
}