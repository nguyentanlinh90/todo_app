package com.ntl.todoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ntl.todoapp.MainActivity
import com.ntl.todoapp.R
import com.ntl.todoapp.adapter.TaskAdapter
import com.ntl.todoapp.listener.IListenerItemTask
import com.ntl.todoapp.listener.IListenerHandleTask
import com.ntl.todoapp.model.Task

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var mActivity: MainActivity? = null
    private var iListenerHandleTask: IListenerHandleTask? = null

    private lateinit var adapterInComplete: TaskAdapter
    private lateinit var adapterComplete: TaskAdapter
    private var mListInComplete = ArrayList<Task>()
    private var mListComplete = ArrayList<Task>()

    private lateinit var rcvInComplete: RecyclerView
    private lateinit var rcvComplete: RecyclerView

    private lateinit var llComplete: LinearLayout
    private lateinit var ivComplete: ImageView
    private lateinit var tvComplete: TextView

    private lateinit var llInComplete: LinearLayout
    private lateinit var ivInComplete: ImageView
    private lateinit var tvInComplete: TextView

    private var isExpandInComplete = true
    private var isExpandComplete = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mActivity = activity as MainActivity?
        iListenerHandleTask = mActivity

        val view = inflater.inflate(R.layout.home_fragment, container, false)

        rcvInComplete = view.findViewById(R.id.rcv_incomplete)
        llInComplete = view.findViewById(R.id.ll_incomplete)
        ivInComplete = view.findViewById(R.id.iv_incomplete)
        tvInComplete = view.findViewById(R.id.tv_incomplete)

        rcvComplete = view.findViewById(R.id.rcv_complete)
        llComplete = view.findViewById(R.id.ll_complete)
        ivComplete = view.findViewById(R.id.iv_complete)
        tvComplete = view.findViewById(R.id.tv_complete)

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
        rcvInComplete.apply {
            isNestedScrollingEnabled = false
            isFocusable = false
            adapter = adapterInComplete
        }

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
        rcvComplete.apply {
            isNestedScrollingEnabled = false
            isFocusable = false
            adapter = adapterComplete
        }
        return view
    }

    fun loadData(tasks: List<Task>) {
        mListInComplete.clear()
        mListComplete.clear()

        for (item in tasks) {
            if (item.isComplete) {
                mListComplete.add(item)
            } else {
                mListInComplete.add(item)
            }
        }
        adapterInComplete.updateAdapter(mListInComplete)
        adapterComplete.updateAdapter(mListComplete)

        showOrHiddenLayoutComplete()
    }

    private fun showOrHiddenLayoutComplete() {
        if (mListInComplete.size > 0) {
            llInComplete.visibility = View.VISIBLE
            rcvInComplete.visibility = View.VISIBLE
            tvInComplete.text =
                String.format(resources.getString(R.string.title_task), mListInComplete.size)
        } else {
            llInComplete.visibility = View.GONE
            rcvInComplete.visibility = View.GONE
        }

        if (mListComplete.size > 0) {
            llComplete.visibility = View.VISIBLE
            tvComplete.text =
                String.format(resources.getString(R.string.title_complete), mListComplete.size)
        } else {
            llComplete.visibility = View.GONE
        }

        llInComplete.setOnClickListener {
            if (isExpandInComplete) {
                isExpandInComplete = false
                rcvInComplete.visibility = View.GONE
                ivInComplete.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            } else {
                isExpandInComplete = true
                rcvInComplete.visibility = View.VISIBLE
                ivInComplete.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
        }

        llComplete.setOnClickListener {
            if (isExpandComplete) {
                isExpandComplete = false
                rcvComplete.visibility = View.GONE
                ivComplete.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            } else {
                isExpandComplete = true
                rcvComplete.visibility = View.VISIBLE
                ivComplete.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
        }
    }
}