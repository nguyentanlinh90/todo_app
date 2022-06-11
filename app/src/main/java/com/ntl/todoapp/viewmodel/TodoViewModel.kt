package com.ntl.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ntl.todoapp.database.TodoDatabase
import com.ntl.todoapp.model.Todo

class TodoViewModel(app: Application) : AndroidViewModel(app) {
    var allTodos: MutableLiveData<List<Todo>> = MutableLiveData()

    init {
        allTodos = MutableLiveData()
        getAllTodo()
    }

    fun getAllTodoObserver(): MutableLiveData<List<Todo>> {
        return allTodos
    }

    private fun getAllTodo() {
        val todoDao = TodoDatabase.getInstance(getApplication())?.getTodoDAO()
        val list = todoDao?.getAllTodo()
        allTodos.postValue(list!!)
    }

    fun insertTodo(todo: Todo) {
        val todoDao = TodoDatabase.getInstance(getApplication())?.getTodoDAO()
        todoDao?.insertTodo(todo)
        getAllTodo()
    }

    fun updateTodo(todo: Todo) {
        val todoDao = TodoDatabase.getInstance(getApplication())?.getTodoDAO()
        todoDao?.updateTodo(todo)
        getAllTodo()
    }

    fun isTodoExists(todo: Todo): Boolean {
        val todoDao = TodoDatabase.getInstance(getApplication())?.getTodoDAO()
        val totoCheck: Todo? = todoDao?.getTodoByName(todo.name)
        return totoCheck != null
    }

    fun deleteTodo(todo: Todo) {
        val todoDao = TodoDatabase.getInstance(getApplication())?.getTodoDAO()
        todoDao?.deleteTodo(todo)
        getAllTodo()
    }

    fun deleteAllTodo() {
        val todoDao = TodoDatabase.getInstance(getApplication())?.getTodoDAO()
        todoDao?.deleteAllTodo()
        getAllTodo()
    }
}