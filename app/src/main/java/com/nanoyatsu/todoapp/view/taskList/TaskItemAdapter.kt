package com.nanoyatsu.todoapp.view.taskList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.databinding.CardTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskItemAdapter(
    private val context: Context
) :
    ListAdapter<Task, TaskItemAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CardTaskBinding>(
            LayoutInflater.from(context), R.layout.card_task, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class ViewHolder(private val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.task = task

            // todo ViewModelに持つ
            binding.completedBox.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    TodoDatabase.getInstance().taskDao().update(task.id, binding.task?.completed as Boolean)
                }
            }

            binding.deleteForeverButton.isEnabled = true
            binding.deleteForeverButton.setOnClickListener {
                it.isEnabled = false
                CoroutineScope(Dispatchers.IO).launch {
                    TodoDatabase.getInstance().taskDao().deleteById(task.id)
                }
            }
            binding.executePendingBindings()
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.label == newItem.label && oldItem.completed == newItem.completed
        }

    }
}