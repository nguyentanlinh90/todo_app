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
import com.ntl.todoapp.common.Logger
import com.ntl.todoapp.listener.IListenerFragmentInitSuccess
import com.ntl.todoapp.listener.IListenerHandleTodo
import com.ntl.todoapp.model.Todo
import com.ntl.todoapp.view.CompleteFragment
import com.ntl.todoapp.view.HomeFragment
import com.ntl.todoapp.view.InCompleteFragment
import com.ntl.todoapp.viewmodel.TodoViewModel
import com.ntl.todoapp.common.MyToast

class MainActivity : AppCompatActivity(), IListenerHandleTodo, IListenerFragmentInitSuccess {
    private val todoInComplete = 1
    private val todoComplete = 2

    private lateinit var vpMain: ViewPager2
    private lateinit var bnvMain: BottomNavigationView
    private lateinit var fabAdd: FloatingActionButton

    private lateinit var adapter: MyViewPagerAdapter

    lateinit var todoViewModel: TodoViewModel

    private var mListTodo: List<Todo> = ArrayList()

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
                    fragment.loadData(mListTodo)
                }
                R.id.menu_incomplete -> {
                    vpMain.currentItem = 1
                    val fragment = supportFragmentManager.fragments[1] as InCompleteFragment
                    fragment.reloadData(getTodoListByType(todoInComplete, mListTodo))
                }
                R.id.menu_complete -> {
                    vpMain.currentItem = 2
                    val fragment = supportFragmentManager.fragments[2] as CompleteFragment
                    fragment.reloadData(getTodoListByType(todoComplete, mListTodo))
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
            showDialogHandleTodo(null)
        }

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
    }

    private fun showDialogHandleTodo(todo: Todo?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_todo)

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

        val edtTodo = dialog.findViewById<EditText>(R.id.edt_task)
        val btnCancel = dialog.findViewById<Button>(R.id.bt_cancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.bt_confirm)
        val tvNote = dialog.findViewById<TextView>(R.id.tv_note)

        if (todo != null) {
            edtTodo.setText(todo.name)
            btnConfirm.text = resources.getString(R.string.update)
        } else {
            btnConfirm.text = resources.getString(R.string.add)
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {

            val strTask = edtTodo.text.toString()

            if (strTask.isEmpty()) {
                tvNote.visibility = View.VISIBLE
                tvNote.text = resources.getString(R.string.please_input_name_of_task)
                return@setOnClickListener
            }
            val todoTemp = Todo(false, strTask)

            if (todoViewModel.isTodoExists(todoTemp)) {
                tvNote.visibility = View.VISIBLE
                tvNote.text = resources.getString(R.string.todo_is_exists)
                return@setOnClickListener
            }

            if (todo != null) {
                todo.name = strTask
                todoViewModel.updateTodo(todo)
                MyToast.showShort(this, resources.getString(R.string.edit_success))
            } else {
                todoViewModel.insertTodo(todoTemp)
                MyToast.showShort(this, resources.getString(R.string.add_success))
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogDeleteTodo(todo: Todo) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete_todo)

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

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            todoViewModel.deleteTodo(todo)
            MyToast.showShort(this, resources.getString(R.string.delete_success))
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getTodoListByType(type: Int, todos: List<Todo>): ArrayList<Todo> {
        val list = ArrayList<Todo>()
        if (type == todoInComplete) {
            for (item in todos) {
                if (!item.isComplete) {
                    list.add(item)
                }
            }
        } else {
            for (item in mListTodo) {
                if (item.isComplete) {
                    list.add(item)
                }
            }
        }

        return list
    }

    override fun onFragmentInitSuccess() {
        todoViewModel.getAllTodoObserver().observe(this) {
            if (supportFragmentManager.fragments.isNotEmpty()) {
                mListTodo = it
                when (vpMain.currentItem) {
                    0 -> {
                        val fragment = supportFragmentManager.fragments[0] as HomeFragment
                        fragment.loadData(it)
                    }
                    1 -> {
                        val fragment = supportFragmentManager.fragments[1] as InCompleteFragment
                        fragment.reloadData(getTodoListByType(todoInComplete, it))
                    }
                    2 -> {
                        val fragment = supportFragmentManager.fragments[2] as CompleteFragment
                        fragment.reloadData(getTodoListByType(todoComplete, it))
                    }
                }
            }
        }
    }

    override fun doUpdateStatus(todo: Todo) {
        todoViewModel.updateTodo(todo)
    }

    override fun doDelete(todo: Todo) {
        showDialogDeleteTodo(todo)
    }

    override fun doEdit(todo: Todo) {
        showDialogHandleTodo(todo)
    }
}