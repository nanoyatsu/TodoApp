package com.nanoyatsu.todoapp.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TaskListFragment() : Fragment() {
    enum class BundleKey { TASK_LIST, FILTER_FUNC }

    private val tasks: ArrayList<Task> = arrayListOf()
    lateinit var filterFunc: ((Task) -> Boolean)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        (container as ViewPager).adapter
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val tasks = arguments?.getParcelableArrayList<Task>(BundleKey.TASK_LIST.name) ?: arrayListOf()
        filterFunc = arguments?.getSerializable(BundleKey.FILTER_FUNC.name) as? ((Task) -> Boolean) ?: { true }
        tasks.addAll(runBlocking(Dispatchers.IO) { TodoDatabase.getInstance().taskDao().getAll().filter(filterFunc) })

        recycler_list.adapter = TaskItemAdapter(activity as AppCompatActivity, tasks) { syncTasks(filterFunc) }
        recycler_list.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        syncTasks(filterFunc)
        recycler_list.adapter?.notifyDataSetChanged()
    }

    private fun syncTasks(filterFunc: ((Task) -> Boolean)) {
        tasks.clear()
        tasks.addAll(runBlocking(Dispatchers.IO) { TodoDatabase.getInstance().taskDao().getAll().filter(filterFunc) })
    }
}