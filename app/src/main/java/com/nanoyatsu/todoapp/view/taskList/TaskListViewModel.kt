package com.nanoyatsu.todoapp.view.taskList

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nanoyatsu.todoapp.TaskFilter
import com.nanoyatsu.todoapp.data.dao.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TaskListViewModel(
    val dao: TaskDao,
    private val filterFunc: TaskFilter
) : ViewModel() {
    private var viewModelJob = Job()
    private var mainScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val tasks = dao.getAllLiveData()

//    private val _taskFilter: MutableLiveData<TaskFilter> = MutableLiveData<TaskFilter>()
//    val taskFilter: LiveData<TaskFilter>
//        get() = _taskFilter

    val filteredTasks = Transformations.map(tasks) { tasks ->
        tasks.filter(filterFunc)
    }

    init {
//        _taskFilter.value = filterFunc
    }
}

// fun <T1, T2, S> combineLatest(source1: LiveData<T1>, source2: LiveData<T2>,
//                              func: (T1?, T2?) -> S?): LiveData<S> {
//    val result = MediatorLiveData<S>()
//    result.addSource(source1, {
//        result.value = func.invoke(source1.value, source2.value)
//    })
//    result.addSource(source2, {
//        result.value = func.invoke(source1.value, source2.value)
//    })
//    return result
//}