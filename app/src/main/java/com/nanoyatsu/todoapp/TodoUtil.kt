package com.nanoyatsu.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.nanoyatsu.todoapp.data.entity.Task

typealias TaskFilter = (Task) -> Boolean

fun <T1, T2, S> combineLatest(
    source1: LiveData<T1>, source2: LiveData<T2>,
    func: (T1?, T2?) -> S?
): LiveData<S> = MediatorLiveData<S>().also { liveData ->
    liveData.addSource(source1) { liveData.value = func(source1.value, source2.value) }
    liveData.addSource(source2) { liveData.value = func(source1.value, source2.value) }
}
