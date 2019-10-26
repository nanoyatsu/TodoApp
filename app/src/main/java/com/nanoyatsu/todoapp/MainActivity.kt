package com.nanoyatsu.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.nanoyatsu.todoapp.data.TodoDatabase
import com.nanoyatsu.todoapp.data.entity.Task
import com.nanoyatsu.todoapp.databinding.ActivityMainBinding
import com.nanoyatsu.todoapp.view.taskList.TabViewAdapter
import com.nanoyatsu.todoapp.view.taskList.TaskListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var tabViewAdapter: TabViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        // リスト表示とタブボタン
        setTodoListFragment(supportFragmentManager)
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        binding.navigation.selectedItemId = R.id.navigation_dashboard

        // タスク追加ボタン
        binding.taskAddButton.setOnClickListener { addTask(binding, binding.taskAddLabelText) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_meta, menu)
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

    private fun addTask(binding: ActivityMainBinding, taskLabel: String?) {
        if (taskLabel.isNullOrEmpty()) {
            Snackbar
                .make(binding.snackbarCoordinator, "Please input your task.", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            return
        }

        runBlocking(Dispatchers.IO) {
            TodoDatabase.getInstance().taskDao().insert(Task(label = taskLabel, completed = false))
        }
        binding.taskAddLabelText = ""
        binding.taskAddLabel.clearFocus()

        // リスト＆画面更新
        tabViewAdapter?.notifyDataSetChanged()
    }

}
