package com.nanoyatsu.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.view.taskList.TabViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val tasks = arrayListOf<Task>()
    private lateinit var tabViewAdapter: TabViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        syncTasks()
        tabViewAdapter = TabViewAdapter(supportFragmentManager, tasks)
        list_container.adapter = tabViewAdapter

        task_add_button.setOnClickListener { addTask(task_add_label.text?.toString()) }
    }

    private fun syncTasks(): ArrayList<Task> {
        tasks.clear()
        tasks.addAll(runBlocking(Dispatchers.IO) { TodoDatabase.getInstance().taskDao().getAll() })
        return tasks
    }

    private fun addTask(taskLabel: String?) {
        if (taskLabel.isNullOrEmpty()) {
            Snackbar
                .make(snackbar_coordinator, "Please input your task.", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            return
        }

        runBlocking(Dispatchers.IO) {
            TodoDatabase.getInstance().taskDao().insert(Task(label = taskLabel, completed = false))
        }
        task_add_label.text?.clear()
        task_add_label.clearFocus()

        // リスト更新
        syncTasks()
        tabViewAdapter.notifyDataSetChanged()
    }


}
