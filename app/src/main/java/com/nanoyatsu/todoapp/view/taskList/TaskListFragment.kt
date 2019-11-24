package com.nanoyatsu.todoapp.view.taskList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.databinding.FragmentTaskListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class TaskListFragment() : Fragment() {
    enum class BundleKey { FILTER_FUNC }

    private lateinit var binding: FragmentTaskListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_task_list,
            container,
            false
        )
        binding.also {
            val factory = TaskListViewModelFactory(taskDao)
            it.vm = ViewModelProvider(this@TaskListFragment, factory).get(TaskListViewModel::class.java)
            it.lifecycleOwner = this@TaskListFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // cast: uncheckだがunsafeではない / 最悪の場合でも{ true }
        @Suppress("UNCHECKED_CAST") val filterFunc =
            arguments?.getSerializable(BundleKey.FILTER_FUNC.name) as? ((Task) -> Boolean) ?: { true }
        if (tasks.isEmpty()) tasks.addAll(runBlocking(Dispatchers.IO) { taskDao.getAll() })

        val adapter = TaskItemAdapter(activity as Context, filterFunc)
        binding.recyclerList.adapter = adapter
        binding.recyclerList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.vm?.tasks?.observe(this, Observer { it?.let { adapter.submitList(it) } })
    }

    // 画面スライド時の表示同期用
    fun reload() {
        val adapter = binding.recyclerList.adapter
        adapter?.notifyItemRangeChanged(0, adapter.itemCount)
    }

    fun filter(func: (Task) -> Boolean) {
        val adapter = binding.recyclerList.adapter as? TaskItemAdapter ?: return
        adapter.filterFunc = func
        adapter.notifyDataSetChanged()
    }

    // 静的変数 TaskListFragmentの各インスタンスで共有することで計算コストを減らす
    companion object {
        val tasks: ArrayList<Task> = arrayListOf()
        var taskDao = TodoDatabase.getInstance().taskDao()
        fun syncTasks() {
            tasks.clear()
            tasks.addAll(runBlocking(Dispatchers.IO) { taskDao.getAll() })
        }
    }
}