package com.nanoyatsu.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.view.taskList.TabViewAdapter
import com.nanoyatsu.todoapp.view.taskList.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var tabViewAdapter: TabViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViewPager(supportFragmentManager)
        task_add_button.setOnClickListener { addTask(task_add_label.text?.toString()) }
    }

    private fun setViewPager(supportFragmentManager: FragmentManager) {
        tabViewAdapter = TabViewAdapter(supportFragmentManager)
        list_container.adapter = tabViewAdapter
        // update ViewPager.SCROLL_STATE_SETTING: スクロール状態の１つで、スクロール中だが遷移先が確定した状態 (なるべく早く描画を更新したい(onResume()とかだとスクロール完了後))
        list_container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_SETTLING)
                    (tabViewAdapter!!
                        .instantiateItem(list_container, list_container.currentItem)
                            as TaskListFragment)
                        .reload()
            }
        })
    }

    private fun addTask(taskLabel: String?) {
        if (taskLabel.isNullOrEmpty()) {
            Snackbar
                .make(snackbar_coordinator, "Please input your task.", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            return
        }

        runBlocking(Dispatchers.IO) {
            TodoDatabase.getInstance().taskDao().insert(Task(label = taskLabel, completed = false))
        }
        task_add_label.text?.clear()
        task_add_label.clearFocus()

        // リスト＆画面更新
        tabViewAdapter?.notifyDataSetChanged()
    }

}
