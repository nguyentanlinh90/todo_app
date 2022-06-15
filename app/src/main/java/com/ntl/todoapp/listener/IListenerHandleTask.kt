package com.ntl.todoapp.listener

import com.ntl.todoapp.model.Task

interface IListenerHandleTask {
    fun doEdit(task: Task)
    fun doUpdateStatus(task: Task)
    fun doDelete(task: Task)

}