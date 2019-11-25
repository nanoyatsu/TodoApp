package com.nanoyatsu.todoapp.view.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nanoyatsu.todoapp.data.dao.TaskDao

class TaskListViewModelFactory(private val dao: TaskDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskListViewModel::class.java))
            return TaskListViewModel(dao) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}