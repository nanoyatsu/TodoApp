package com.nanoyatsu.todoapp.view.taskList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.nanoyatsu.todoapp.data.entity.Task
import java.io.Serializable

class TabViewAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    enum class Tabs(val filterFunc: (Task) -> Boolean) { ACTIVE({ !it.completed }), ALL({ true }), COMPLETED({ it.completed }) }

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle().also {
            it.putSerializable(
                TaskListFragment.BundleKey.FILTER_FUNC.name,
                Tabs.values()[position].filterFunc as Serializable
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

    // 常に「非表示のポジション」を返す 現在位置notifyDatasetChanged()のたびに更新するようになる(計算コストが上がる)
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun notifyDataSetChanged() {
        TaskListFragment.syncTasks()
        super.notifyDataSetChanged()
    }
}
