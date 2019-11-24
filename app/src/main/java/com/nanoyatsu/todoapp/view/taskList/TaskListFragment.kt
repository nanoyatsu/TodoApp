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

        // cast: uncheckだがunsafeではない / 最悪の場合でも{ true }
        @Suppress("UNCHECKED_CAST") val filterFunc =
            arguments?.getSerializable(BundleKey.FILTER_FUNC.name) as? ((Task) -> Boolean) ?: { true }
        binding.also {
            val factory = TaskListViewModelFactory(taskDao, filterFunc)
            it.vm = ViewModelProvider(this@TaskListFragment, factory).get(TaskListViewModel::class.java)
            it.lifecycleOwner = this@TaskListFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = TaskItemAdapter(activity as Context)
        binding.recyclerList.adapter = adapter
        binding.recyclerList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.vm?.filteredTasks?.observe(this, Observer { it?.let { adapter.submitList(it) } })
    }

    // 静的変数 TaskListFragmentの各インスタンスで共有することで計算コストを減らす
    companion object {
        var taskDao = TodoDatabase.getInstance().taskDao()
    }
}