package com.nanoyatsu.todoapp.view.taskList

import androidx.lifecycle.ViewModel
import com.nanoyatsu.todoapp.data.dao.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TaskListViewModel(val dao: TaskDao) : ViewModel() {
    private var viewModelJob = Job()
    private var mainScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val tasks = dao.getAllLiveData()
//    private val _tasks = MutableLiveData<List<Task>>()
//    val tasks: LiveData<List<Task>>
//        get() = _tasks



}