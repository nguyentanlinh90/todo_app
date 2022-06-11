package com.ntl.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.ntl.todoapp.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun getTodoDAO(): TodoDAO

    companion object {
        private const val DATABASE_NAME = "todo.db"

        private var instance: TodoDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): TodoDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}