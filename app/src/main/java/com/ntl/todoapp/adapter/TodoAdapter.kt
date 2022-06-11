package com.ntl.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntl.todoapp.R
import com.ntl.todoapp.listener.IListenerItemTodo
import com.ntl.todoapp.model.Todo

class TodoAdapter(todos: List<Todo>, private val listener: IListenerItemTodo) :
    RecyclerView.Adapter<TodoAdapter.ItemViewHolder>() {

    private var mList = todos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ItemViewHolder(view, listener)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(itemView: View, private val listener: IListenerItemTodo) :
        RecyclerView.ViewHolder(itemView) {
        private val cbTodo: CheckBox = itemView.findViewById(R.id.cb_todo)
        private val tvTodo: TextView = itemView.findViewById(R.id.tv_todo)
        private val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)
        private val ivEdit: ImageView = itemView.findViewById(R.id.iv_edit)

        fun bind(todo: Todo) {
            cbTodo.isChecked = todo.isComplete
            tvTodo.text = todo.name

            cbTodo.setOnClickListener {
                listener.onClickStatusTodo(todo)
            }

            ivDelete.setOnClickListener {
                listener.onClickDeleteTodo(todo)
            }

            ivEdit.setOnClickListener {
                listener.onClickEditTodo(todo)
            }
        }

    }

    fun updateAdapter(todos: List<Todo>) {
        this.mList = todos
        notifyDataSetChanged()
    }
}