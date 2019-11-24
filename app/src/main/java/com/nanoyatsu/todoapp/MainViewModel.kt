package com.nanoyatsu.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _eventAddTask = MutableLiveData<Boolean>()
    val eventAddTask: LiveData<Boolean>
        get() = _eventAddTask

    init {
        _eventAddTask.value = false
    }


    fun onAddTask() {
        _eventAddTask.value = true
    }

    fun onAddTaskComplete(): Unit {
        _eventAddTask.value = false
    }
}