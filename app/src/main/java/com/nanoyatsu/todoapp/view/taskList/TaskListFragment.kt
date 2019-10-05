package com.nanoyatsu.todoapp.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.entity.Task
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment() : Fragment() {
    enum class BundleKey { TASK_LIST }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val tasks = arguments?.getParcelableArrayList<Task>(BundleKey.TASK_LIST.name) ?: arrayListOf()
        recycler_list.adapter = TaskItemAdapter(activity as AppCompatActivity, tasks)
        recycler_list.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
    }
}