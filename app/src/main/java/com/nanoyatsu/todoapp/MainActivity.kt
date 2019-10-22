package com.nanoyatsu.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.view.taskList.TabViewAdapter
import com.nanoyatsu.todoapp.view.taskList.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.navigation
import kotlinx.android.synthetic.main.activity_todo_tab.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var tabViewAdapter: TabViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // リスト表示とタブボタン
        setTodoListFragment(supportFragmentManager)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // タスク追加ボタン
        task_add_button.setOnClickListener { addTask(task_add_label.text?.toString()) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.clear_completed) {
            runBlocking(Dispatchers.IO) { TodoDatabase.getInstance().taskDao().deleteCompleted() }
            tabViewAdapter?.notifyDataSetChanged()
            return true
        }

        return super.onOptionsItemSelected(item)
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
