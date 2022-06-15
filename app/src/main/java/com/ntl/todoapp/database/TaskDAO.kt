package com.ntl.todoapp.database

import androidx.room.*
import com.ntl.todoapp.model.Task

@Dao
interface TaskDAO {

    @Insert
    fun insertTask(task: Task?)

    @Query("SELECT * FROM task")
    fun getAllTask(): List<Task>?

    @Query("SELECT * FROM task where name= :name")
    fun getTaskByName(name: String?): Task?

    @Update
    fun updateTask(task: Task?)

    @Delete
    fun deleteTask(task: Task?)

    @Query("DELETE FROM task")
    fun deleteAllTask()
}