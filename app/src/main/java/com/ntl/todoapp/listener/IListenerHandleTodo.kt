package com.ntl.todoapp.listener

import com.ntl.todoapp.model.Todo

interface IListenerHandleTodo {
    fun doUpdateStatus(todo: Todo)
    fun doDelete(todo: Todo)
    fun doEdit(todo: Todo)
}