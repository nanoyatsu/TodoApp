package com.nanoyatsu.todoapp.view.taskList

import android.view.MenuItem
import androidx.lifecycle.*
import com.nanoyatsu.todoapp.R
import com.nanoyatsu.todoapp.TaskFilter
import com.nanoyatsu.todoapp.combineLatest
import com.nanoyatsu.todoapp.data.dao.TaskDao
import com.nanoyatsu.todoapp.data.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TaskListViewModel(val dao: TaskDao) : ViewModel() {
    private var viewModelJob = Job()
    private var mainScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val tasks = dao.getAllLiveData()

    private val _taskFilter = MutableLiveData<TaskFilter>()
    val taskFilter: LiveData<TaskFilter>
        get() = _taskFilter

    val filteredTasks = combineLatest(tasks, taskFilter, this::doFilter)

    init {
        _taskFilter.value = { true }
    }

    private fun doFilter(tasks: List<Task>?, taskFilter: TaskFilter?): List<Task> {
        if (tasks == null) return listOf()
        if (taskFilter == null) return tasks
        return tasks.filter(taskFilter)
    }

    fun navigationItemSelectedListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_unchecked -> _taskFilter.value = { !it.completed }
            R.id.navigation_all -> _taskFilter.value = { true }
            R.id.navigation_checked -> _taskFilter.value = { it.completed }
        }
        return true
    }
}
