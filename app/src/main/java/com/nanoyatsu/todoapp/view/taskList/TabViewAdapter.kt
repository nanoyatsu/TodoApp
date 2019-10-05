package com.nanoyatsu.todoapp.view.taskList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nanoyatsu.todoapp.data.entity.Task

class TabViewAdapter(fm: FragmentManager, private val tasks: ArrayList<Task>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    enum class Tabs(val filterFunc: (Task) -> Boolean) { ACTIVE({ !it.completed }), ALL({ true }), COMPLETED({ it.completed }) }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle().also {
            val list = arrayListOf<Task>()
            list.addAll(tasks.filter(Tabs.values()[position].filterFunc))
            it.putParcelableArrayList(
                TaskListFragment.BundleKey.TASK_LIST.name, ArrayList(tasks.filter(Tabs.values()[position].filterFunc))
            )
        }
        return TaskListFragment().also { it.arguments = bundle }

    }

    override fun getCount(): Int {
        return Tabs.values().size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return Tabs.values()[position].name
    }
}
