package com.ntl.todoapp.listener

import com.ntl.todoapp.model.Task

interface IListenerItemTask {
    fun onClickStatusTask(task: Task)
    fun onClickDeleteTask(task: Task)
    fun onClickEditTask(task: Task)
}