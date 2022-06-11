package com.ntl.todoapp.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ntl.todoapp.model.Todo
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest : TestCase() {
    private lateinit var db: TodoDatabase
    private lateinit var dao: TodoDAO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getTodoDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    //test case to insert _todo in room database
    @Test
    fun insertTodo() = runBlocking {
        val todo = Todo(false, "Test insertTodo")
        dao.insertTodo(todo)
        val todos = dao.getAllTodo()
        assertThat(todos?.contains(todo)).isTrue()
    }

    //test case to delete _todo in room database
    @Test
    fun deleteTodo() = runBlocking {
        val nameTodoAdd = "Test deleteTodo"
        val todo = Todo(false, nameTodoAdd)
        dao.insertTodo(todo)

        val todoDelete = dao.getTodoByName(nameTodoAdd)
        dao.deleteTodo(todoDelete)

        val todos = dao.getAllTodo()
        assertThat(todos).doesNotContain(todo)
    }

    //test case to edit _todo in room database
    @Test
    fun editTodo() = runBlocking {
        val nameTodoAdd = "Test editTodo"
        val todo = Todo(false, nameTodoAdd)
        dao.insertTodo(todo)

        val newTodo = dao.getTodoByName(nameTodoAdd)
        newTodo?.name = "New $nameTodoAdd"
        dao.updateTodo(newTodo)

        val todos = dao.getAllTodo()
        assertThat(todos?.contains(newTodo)).isTrue()
    }
}