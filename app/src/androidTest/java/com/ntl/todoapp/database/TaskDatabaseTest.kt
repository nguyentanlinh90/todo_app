package com.ntl.todoapp.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ntl.todoapp.model.Task
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
class TaskDatabaseTest : TestCase() {
    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDAO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getTaskDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    //test case to insert _todo in room database
    @Test
    fun insertTodo() = runBlocking {
        val todo = Task(false, "Test insertTodo")
        dao.insertTask(todo)
        val todos = dao.getAllTask()
        assertThat(todos?.contains(todo)).isTrue()
    }

    //test case to delete _todo in room database
    @Test
    fun deleteTodo() = runBlocking {
        val nameTodoAdd = "Test deleteTodo"
        val todo = Task(false, nameTodoAdd)
        dao.insertTask(todo)

        val todoDelete = dao.getTaskByName(nameTodoAdd)
        dao.deleteTask(todoDelete)

        val todos = dao.getAllTask()
        assertThat(todos).doesNotContain(todo)
    }

    //test case to edit _todo in room database
    @Test
    fun editTodo() = runBlocking {
        val nameTodoAdd = "Test editTodo"
        val todo = Task(false, nameTodoAdd)
        dao.insertTask(todo)

        val newTodo = dao.getTaskByName(nameTodoAdd)
        newTodo?.name = "New $nameTodoAdd"
        dao.updateTask(newTodo)

        val todos = dao.getAllTask()
        assertThat(todos?.contains(newTodo)).isTrue()
    }
}