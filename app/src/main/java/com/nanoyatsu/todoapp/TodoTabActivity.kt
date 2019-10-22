package com.nanoyatsu.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nanoyatsu.todoapp.view.taskList.TaskListFragment
import kotlinx.android.synthetic.main.activity_todo_tab.*

class TodoTabActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_tab)
        setTodoListFragment(supportFragmentManager)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setTodoListFragment(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().also {
            it.add(R.id.todo_list_container, TaskListFragment())
            it.commit()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val taskList = supportFragmentManager.fragments.firstOrNull() as? TaskListFragment
            ?: return@OnNavigationItemSelectedListener false
        when (item.itemId) {
            R.id.navigation_home -> {
                taskList.filter { !it.completed }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                taskList.filter { true }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                taskList.filter { it.completed }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


}
