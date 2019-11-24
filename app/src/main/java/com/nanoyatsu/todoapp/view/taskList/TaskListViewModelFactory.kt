package com.nanoyatsu.todoapp.view.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nanoyatsu.todoapp.TaskFilter
import com.nanoyatsu.todoapp.data.dao.TaskDao

class TaskListViewModelFactory(
    private val dao: TaskDao,
    private val filterFunc: TaskFilter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java))
            return TaskListViewModel(dao, filterFunc) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}