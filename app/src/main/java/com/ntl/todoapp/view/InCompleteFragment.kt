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
import com.ntl.todoapp.listener.IListenerFragmentInitSuccess
import com.ntl.todoapp.listener.IListenerHandleTask
import com.ntl.todoapp.listener.IListenerItemTask
import com.ntl.todoapp.model.Task

class InCompleteFragment : Fragment() {

    companion object {
        fun newInstance() = InCompleteFragment()
    }

    private var mActivity: MainActivity? = null
    private var iListenerHandleTask: IListenerHandleTask? = null

    private var iListenerFragmentInitSuccess: IListenerFragmentInitSuccess? = null

    private lateinit var adapterInComplete: TaskAdapter
    private var mListInComplete: List<Task> = ArrayList()

    private lateinit var rcvInComplete: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mActivity = activity as MainActivity?
        iListenerHandleTask = mActivity
        iListenerFragmentInitSuccess = mActivity

        val view = inflater.inflate(R.layout.incomplete_fragment, container, false)

        rcvInComplete = view.findViewById(R.id.rcv_incomplete)

        adapterInComplete = TaskAdapter(mListInComplete, object : IListenerItemTask {
            override fun onClickStatusTask(task: Task) {
                task.isComplete = true
                iListenerHandleTask?.doUpdateStatus(task)
            }

            override fun onClickDeleteTask(task: Task) {
                iListenerHandleTask?.doDelete(task)
            }

            override fun onClickEditTask(task: Task) {
                iListenerHandleTask?.doEdit(task)
            }
        })
        rcvInComplete.adapter = adapterInComplete

        iListenerFragmentInitSuccess?.onFragmentInitSuccess()

        return view
    }

    fun reloadData(tasks: List<Task>) {
        mListInComplete = tasks
        adapterInComplete.updateAdapter(mListInComplete)
    }
}