package com.ntl.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntl.todoapp.R
import com.ntl.todoapp.listener.IListenerItemTask
import com.ntl.todoapp.model.Task

class TaskAdapter(tasks: List<Task>, private val listener: IListenerItemTask) :
    RecyclerView.Adapter<TaskAdapter.ItemViewHolder>() {

    private var mList = tasks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ItemViewHolder(view, listener)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(itemView: View, private val listener: IListenerItemTask) :
        RecyclerView.ViewHolder(itemView) {
        private val cbTask: CheckBox = itemView.findViewById(R.id.cb_task)
        private val tvTask: TextView = itemView.findViewById(R.id.tv_task)
        private val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)
        private val ivEdit: ImageView = itemView.findViewById(R.id.iv_edit)

        fun bind(task: Task) {
            cbTask.isChecked = task.isComplete
            tvTask.text = task.name

            cbTask.setOnClickListener {
                listener.onClickStatusTask(task)
            }

            ivDelete.setOnClickListener {
                listener.onClickDeleteTask(task)
            }

            ivEdit.setOnClickListener {
                listener.onClickEditTask(task)
            }
        }

    }

    fun updateAdapter(tasks: List<Task>) {
        this.mList = tasks
        notifyDataSetChanged()
    }
}