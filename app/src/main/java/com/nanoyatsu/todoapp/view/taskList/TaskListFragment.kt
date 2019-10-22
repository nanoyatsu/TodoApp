package com.nanoyatsu.todoapp.view.taskList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TaskListFragment() : Fragment() {
    enum class BundleKey { FILTER_FUNC }

    // 静的変数 TaskListFragmentの各インスタンスで共有することで計算コストを減らす
    companion object {
        val tasks: ArrayList<Task> = arrayListOf()
        var taskDao = TodoDatabase.getInstance().taskDao()
        fun syncTasks() {
            tasks.clear()
            tasks.addAll(runBlocking(Dispatchers.IO) { taskDao.getAll() })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // cast: uncheckだがunsafeではない / 最悪の場合でも{ true }
        @Suppress("UNCHECKED_CAST") val filterFunc =
            arguments?.getSerializable(BundleKey.FILTER_FUNC.name) as? ((Task) -> Boolean) ?: { true }
        if (tasks.isEmpty()) tasks.addAll(runBlocking(Dispatchers.IO) { taskDao.getAll() })

        recycler_list.adapter = TaskItemAdapter(activity as Context, tasks, filterFunc)
        recycler_list.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }

    // 画面スライド時の表示同期用
    fun reload() {
        val adapter = recycler_list.adapter
        adapter?.notifyItemRangeChanged(0, adapter.itemCount)
    }

    fun filter(func: (Task) -> Boolean) {
        val adapter = recycler_list.adapter as? TaskItemAdapter ?: return
        adapter.filterFunc = func
        adapter.notifyDataSetChanged()
    }
}