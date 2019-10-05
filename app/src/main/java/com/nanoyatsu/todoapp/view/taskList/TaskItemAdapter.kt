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
import com.nanoyatsu.todoapp.data.entity.Task

class TaskItemAdapter(private val context: Context, private val tasks: ArrayList<Task>) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val base = LayoutInflater.from(context)
            .inflate(R.layout.card_task, parent, false) as CardView
        return ViewHolder(base)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setContent(holder, tasks[position])
    }

    private fun setContent(holder: ViewHolder, task: Task) {
        holder.completedBox.isChecked = task.completed
        holder.label.text = task.label

        holder.completedBox.setOnClickListener {
            // todo データ更新
        }
        holder.deleteForeverButton.setOnClickListener {
            // todo 削除ボタン
        }
    }

    class ViewHolder(base: CardView) : RecyclerView.ViewHolder(base) {
        val completedBox: CheckBox = base.findViewById(R.id.completed_box)
        val label: TextView = base.findViewById(R.id.label)
        val deleteForeverButton: ImageButton = base.findViewById(R.id.delete_forever_button)
    }
}