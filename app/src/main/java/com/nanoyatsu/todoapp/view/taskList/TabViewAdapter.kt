package com.nanoyatsu.todoapp.view.taskList

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabViewAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    enum class Tabs { ACTIVE, ALL, COMPLETED }

    override fun getItem(position: Int): Fragment {
        return TaskListFragment()
    }

    override fun getCount(): Int {
        return Tabs.values().size
    }

}
