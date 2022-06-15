package com.ntl.todoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ntl.todoapp.MainActivity
import com.ntl.todoapp.R
import com.ntl.todoapp.adapter.TaskAdapter
import com.ntl.todoapp.listener.IListenerItemTask
import com.ntl.todoapp.listener.IListenerHandleTask
import com.ntl.todoapp.model.Task

class CompleteFragment : Fragment() {

    companion object {
        fun newInstance() = CompleteFragment()
    }

    private var mActivity: MainActivity? = null
    private var iListenerHandleTask: IListenerHandleTask? = null

    private lateinit var adapterComplete: TaskAdapter
    private var mListComplete = ArrayList<Task>()

    private lateinit var rcvComplete: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mActivity = activity as MainActivity?
        iListenerHandleTask = mActivity

        val view = inflater.inflate(R.layout.complete_fragment, container, false)

        rcvComplete = view.findViewById(R.id.rcv_complete)

        adapterComplete = TaskAdapter(mListComplete, object : IListenerItemTask {
            override fun onClickStatusTask(task: Task) {
                task.isComplete = false
                iListenerHandleTask?.doUpdateStatus(task)
            }

            override fun onClickDeleteTask(task: Task) {
                iListenerHandleTask?.doDelete(task)
            }

            override fun onClickEditTask(task: Task) {
                iListenerHandleTask?.doEdit(task)
            }
        })
        rcvComplete.adapter = adapterComplete

        return view
    }

    fun reloadData(tasks: ArrayList<Task>) {
        mListComplete = tasks
        adapterComplete.updateAdapter(mListComplete)
    }
}