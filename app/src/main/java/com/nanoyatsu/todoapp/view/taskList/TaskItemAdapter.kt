package com.nanoyatsu.todoapp.view.taskList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.databinding.CardTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskItemAdapter(
    private val context: Context,
    private val tasks: ArrayList<Task>,
    var filterFunc: ((Task) -> Boolean)
) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CardTaskBinding>(
            LayoutInflater.from(context), R.layout.card_task, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasks.filter(filterFunc).size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks.filter(filterFunc)[position]
        holder.binding.task = task

        val tasksIndex = tasks.indexOf(task)

//        holder.completedBox.isEnabled = true
        holder.binding.completedBox.isEnabled = true
//        holder.completedBox.setOnClickListener {
        holder.binding.completedBox.setOnClickListener {
            it.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                TodoDatabase.getInstance().taskDao().update(task.id, holder.binding.task?.completed as Boolean)
            }
            val realtimePosition = tasks.filter(filterFunc).indexOf(task)
//            task.completed = holder.completedBox.isChecked
            task.completed = holder.binding.task?.completed as Boolean
            tasks[tasksIndex] = task
            if (filterFunc(task)) notifyItemChanged(realtimePosition)
            else notifyItemRemoved(realtimePosition)
        }

//        holder.deleteForeverButton.isEnabled = true
        holder.binding.deleteForeverButton.isEnabled = true
//        holder.deleteForeverButton.setOnClickListener {
        holder.binding.deleteForeverButton.setOnClickListener {
            it.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                TodoDatabase.getInstance().taskDao().deleteById(task.id)
            }
            notifyItemRemoved(tasks.filter(filterFunc).indexOf(tasks[tasksIndex]))
            tasks.removeAt(tasksIndex)
        }
        holder.binding.executePendingBindings()
    }

    class ViewHolder(val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {}
}