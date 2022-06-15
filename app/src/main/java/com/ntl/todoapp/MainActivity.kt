package com.ntl.todoapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ntl.todoapp.adapter.MyViewPagerAdapter
import com.ntl.todoapp.listener.IListenerFragmentInitSuccess
import com.ntl.todoapp.listener.IListenerHandleTask
import com.ntl.todoapp.model.Task
import com.ntl.todoapp.view.CompleteFragment
import com.ntl.todoapp.view.HomeFragment
import com.ntl.todoapp.view.InCompleteFragment
import com.ntl.todoapp.viewmodel.TaskViewModel
import com.ntl.todoapp.common.MyToast
import com.ntl.todoapp.util.Constants

class MainActivity : AppCompatActivity(), IListenerHandleTask, IListenerFragmentInitSuccess {

    private lateinit var vpMain: ViewPager2
    private lateinit var bnvMain: BottomNavigationView
    private lateinit var fabAdd: FloatingActionButton

    private lateinit var adapter: MyViewPagerAdapter

    lateinit var taskViewModel: TaskViewModel

    private var mListTask: List<Task> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vpMain = findViewById(R.id.vp2_main)
        bnvMain = findViewById(R.id.bnv_main)
        fabAdd = findViewById(R.id.fab_add)

        initView()

        initEvent()
    }

    private fun initView() {
        adapter = MyViewPagerAdapter(this)
        vpMain.adapter = adapter
        vpMain.offscreenPageLimit = 3
        bnvMain.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_home -> {
                    vpMain.currentItem = 0
                    val fragment = supportFragmentManager.fragments[0] as HomeFragment
                    fragment.loadData(mListTask)
                }
                R.id.menu_incomplete -> {
                    vpMain.currentItem = 1
                    val fragment = supportFragmentManager.fragments[1] as InCompleteFragment
                    fragment.reloadData(sortTaskListByStatus(!Constants.isTaskComplete, mListTask))
                }
                R.id.menu_complete -> {
                    vpMain.currentItem = 2
                    val fragment = supportFragmentManager.fragments[2] as CompleteFragment
                    fragment.reloadData(sortTaskListByStatus(Constants.isTaskComplete, mListTask))
                }
            }
            true
        }

        vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> bnvMain.menu.findItem(R.id.menu_home).isChecked = true
                    1 -> bnvMain.menu.findItem(R.id.menu_incomplete).isChecked = true
                    2 -> bnvMain.menu.findItem(R.id.menu_complete).isChecked = true
                }
            }
        })
    }


    private fun initEvent() {
        fabAdd.setOnClickListener {
            showDialogHandleTask(!Constants.isTaskDelete, null)
        }

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private fun showDialogHandleTask(isDelete: Boolean, task: Task?) {
        //task = null: insert, task != null: update
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        if (isDelete) {
            dialog.setContentView(R.layout.dialog_delete_task)
        } else {
            dialog.setContentView(R.layout.dialog_add_task)
        }

        val window = dialog.window ?: return

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.CENTER

        window.attributes = layoutParams

        dialog.setCancelable(false)

        val btnCancel = dialog.findViewById<Button>(R.id.bt_cancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.bt_confirm)

        if (isDelete) {
            btnConfirm.setOnClickListener {
                if (task != null) {
                    taskViewModel.deleteTask(task)
                    MyToast.short(this, resources.getString(R.string.delete_success))
                }
                dialog.dismiss()
            }
        } else {
            val edtTask = dialog.findViewById<EditText>(R.id.edt_task)
            val tvNote = dialog.findViewById<TextView>(R.id.tv_note)

            if (task != null) {
                edtTask.setText(task.name)
                btnConfirm.text = resources.getString(R.string.update)
            } else {
                btnConfirm.text = resources.getString(R.string.add)
            }

            btnConfirm.setOnClickListener {

                val strTask = edtTask.text.toString()

                if (strTask.isEmpty()) {
                    tvNote.visibility = View.VISIBLE
                    tvNote.text = resources.getString(R.string.please_input_name_of_task)
                    return@setOnClickListener
                }

                val taskTemp = Task(false, strTask)

                if (taskViewModel.isTaskExists(taskTemp)) {
                    tvNote.visibility = View.VISIBLE
                    tvNote.text = resources.getString(R.string.task_is_exists)
                    return@setOnClickListener
                }

                if (task != null) {
                    task.name = strTask
                    taskViewModel.updateTask(task)
                    MyToast.short(this, resources.getString(R.string.edit_success))
                } else {
                    taskViewModel.insertTask(taskTemp)
                    MyToast.short(this, resources.getString(R.string.add_success))
                }
                dialog.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun sortTaskListByStatus(status: Boolean, tasks: List<Task>): ArrayList<Task> {
        val list = ArrayList<Task>()
        for (item in tasks) {
            if (item.isComplete == status) {
                list.add(item)
            }
        }
        return list
    }

    override fun onFragmentInitSuccess() {
        taskViewModel.getAllTaskObserver().observe(this) {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                mListTask = it
                when (vpMain.currentItem) {
                    0 -> {
                        val fragment = supportFragmentManager.fragments[0] as HomeFragment
                        fragment.loadData(it)
                    }
                    1 -> {
                        val fragment = supportFragmentManager.fragments[1] as InCompleteFragment
                        fragment.reloadData(sortTaskListByStatus(!Constants.isTaskComplete, it))
                    }
                    2 -> {
                        val fragment = supportFragmentManager.fragments[2] as CompleteFragment
                        fragment.reloadData(sortTaskListByStatus(Constants.isTaskComplete, it))
                    }
                }
            }
        }
    }

    override fun doUpdateStatus(task: Task) {
        taskViewModel.updateTask(task)
    }

    override fun doDelete(task: Task) {
        showDialogHandleTask(Constants.isTaskDelete, task)
    }

    override fun doEdit(task: Task) {
        showDialogHandleTask(!Constants.isTaskDelete, task)
    }
}