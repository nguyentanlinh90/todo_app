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
import com.ntl.todoapp.listener.IListenerFragmentInitSuccess
import com.ntl.todoapp.listener.IListenerHandleTodo
import com.ntl.todoapp.listener.IListenerItemTodo
import com.ntl.todoapp.model.Todo

class InCompleteFragment : Fragment() {

    companion object {
        fun newInstance() = InCompleteFragment()
    }

    private var mActivity: MainActivity? = null
    private var iListenerHandleTodo: IListenerHandleTodo? = null

    private var iListenerFragmentInitSuccess: IListenerFragmentInitSuccess? = null

    private lateinit var adapterInComplete: TodoAdapter
    private var mListInComplete: List<Todo> = ArrayList()

    private lateinit var rcvInComplete: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mActivity = activity as MainActivity?
        iListenerHandleTodo = mActivity
        iListenerFragmentInitSuccess = mActivity

        val view = inflater.inflate(R.layout.incomplete_fragment, container, false)

        rcvInComplete = view.findViewById(R.id.rcv_incomplete)

        adapterInComplete = TodoAdapter(mListInComplete, object : IListenerItemTodo {
            override fun onClickStatusTodo(todo: Todo) {
                todo.isComplete = true
                iListenerHandleTodo?.doUpdateStatus(todo)
            }

            override fun onClickDeleteTodo(todo: Todo) {
                iListenerHandleTodo?.doDelete(todo)
            }

            override fun onClickEditTodo(todo: Todo) {
                iListenerHandleTodo?.doEdit(todo)
            }
        })
        rcvInComplete.adapter = adapterInComplete

        iListenerFragmentInitSuccess?.onFragmentInitSuccess()

        return view
    }

    fun reloadData(todos: List<Todo>) {
        mListInComplete = todos
        adapterInComplete.updateAdapter(mListInComplete)
    }
}