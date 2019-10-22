package com.nanoyatsu.todoapp.view.taskList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
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
        val base = LayoutInflater.from(context)
            .inflate(R.layout.card_task, parent, false) as CardView
        return ViewHolder(base)
    }

    override fun getItemCount(): Int {
        return tasks.filter(filterFunc).size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks.filter(filterFunc)[position]
        holder.completedBox.isChecked = task.completed
        holder.label.text = task.label

        val tasksIndex = tasks.indexOf(task)

        holder.completedBox.isEnabled = true
        holder.completedBox.setOnClickListener {
            it.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                TodoDatabase.getInstance().taskDao().update(task.id, holder.completedBox.isChecked)
            }
            val realtimePosition = tasks.filter(filterFunc).indexOf(task)
            task.completed = holder.completedBox.isChecked
            tasks[tasksIndex] = task
            if (filterFunc(task)) notifyItemChanged(realtimePosition)
            else notifyItemRemoved(realtimePosition)
        }

        holder.deleteForeverButton.isEnabled = true
        holder.deleteForeverButton.setOnClickListener {
            it.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                TodoDatabase.getInstance().taskDao().deleteById(task.id)
            }
            notifyItemRemoved(tasks.filter(filterFunc).indexOf(tasks[tasksIndex]))
            tasks.removeAt(tasksIndex)
        }
    }

    class ViewHolder(base: CardView) : RecyclerView.ViewHolder(base) {
        val completedBox: CheckBox = base.findViewById(R.id.completed_box)
        val label: TextView = base.findViewById(R.id.label)
        val deleteForeverButton: ImageButton = base.findViewById(R.id.delete_forever_button)
    }
}