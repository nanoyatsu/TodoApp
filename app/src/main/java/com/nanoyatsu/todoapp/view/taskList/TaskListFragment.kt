package com.nanoyatsu.todoapp.view.taskList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.data.entity.Task

class TaskListFragment() : Fragment() {
    enum class BundleKey { TASK_LIST }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val tasks = arguments?.getParcelableArrayList<Task>(BundleKey.TASK_LIST.name) ?: arrayListOf()


        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }
}