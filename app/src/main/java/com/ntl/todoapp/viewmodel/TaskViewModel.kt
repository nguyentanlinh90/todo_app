package com.ntl.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ntl.todoapp.database.TaskDatabase
import com.ntl.todoapp.model.Task

class TaskViewModel(app: Application) : AndroidViewModel(app) {
    var allTasks: MutableLiveData<List<Task>> = MutableLiveData()

    init {
        allTasks = MutableLiveData()
        getAllTask()
    }

    fun getAllTaskObserver(): MutableLiveData<List<Task>> {
        return allTasks
    }

    private fun getAllTask() {
        val taskDao = TaskDatabase.getInstance(getApplication())?.getTaskDAO()
        val list = taskDao?.getAllTask()
        allTasks.postValue(list!!)
    }

    fun insertTask(task: Task) {
        val taskDao = TaskDatabase.getInstance(getApplication())?.getTaskDAO()
        taskDao?.insertTask(task)
        getAllTask()
    }

    fun updateTask(task: Task) {
        val taskDao = TaskDatabase.getInstance(getApplication())?.getTaskDAO()
        taskDao?.updateTask(task)
        getAllTask()
    }

    fun isTaskExists(task: Task): Boolean {
        val taskDao = TaskDatabase.getInstance(getApplication())?.getTaskDAO()
        val taskCheck: Task? = taskDao?.getTaskByName(task.name)
        return taskCheck != null
    }

    fun deleteTask(task: Task) {
        val taskDao = TaskDatabase.getInstance(getApplication())?.getTaskDAO()
        taskDao?.deleteTask(task)
        getAllTask()
    }

    fun deleteAllTask() {
        val taskDao = TaskDatabase.getInstance(getApplication())?.getTaskDAO()
        taskDao?.deleteAllTask()
        getAllTask()
    }
}