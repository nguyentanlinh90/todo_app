package com.ntl.todoapp.listener

import com.ntl.todoapp.model.Todo

interface IListenerItemTodo {
    fun onClickStatusTodo(todo: Todo)
    fun onClickDeleteTodo(todo: Todo)
    fun onClickEditTodo(todo: Todo)
}