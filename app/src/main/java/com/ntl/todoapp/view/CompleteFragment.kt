package com.ntl.todoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ntl.todoapp.MainActivity
import com.ntl.todoapp.R
import com.ntl.todoapp.adapter.TodoAdapter
import com.ntl.todoapp.listener.IListenerItemTodo
import com.ntl.todoapp.listener.IListenerHandleTodo
import com.ntl.todoapp.model.Todo

class CompleteFragment : Fragment() {

    companion object {
        fun newInstance() = CompleteFragment()
    }

    private var mActivity: MainActivity? = null
    private var iListenerHandleTodo: IListenerHandleTodo? = null

    private lateinit var adapterComplete: TodoAdapter
    private var mListComplete = ArrayList<Todo>()

    private lateinit var rcvComplete: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mActivity = activity as MainActivity?
        iListenerHandleTodo = mActivity

        val view = inflater.inflate(R.layout.complete_fragment, container, false)

        rcvComplete = view.findViewById(R.id.rcv_complete)

        adapterComplete = TodoAdapter(mListComplete, object : IListenerItemTodo {
            override fun onClickStatusTodo(todo: Todo) {
                todo.isComplete = false
                iListenerHandleTodo?.doUpdateStatus(todo)
            }

            override fun onClickDeleteTodo(todo: Todo) {
                iListenerHandleTodo?.doDelete(todo)
            }

            override fun onClickEditTodo(todo: Todo) {
                iListenerHandleTodo?.doEdit(todo)
            }
        })
        rcvComplete.adapter = adapterComplete

        return view
    }

    fun reloadData(todos: ArrayList<Todo>) {
        mListComplete = todos
        adapterComplete.updateAdapter(mListComplete)
    }
}