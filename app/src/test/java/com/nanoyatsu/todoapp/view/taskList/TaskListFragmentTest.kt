package com.nanoyatsu.todoapp.view.taskList

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nanoyatsu.todoapp.data.dao.TaskDao
import com.nanoyatsu.todoapp.data.entity.Task
import kotlinx.android.synthetic.main.fragment_task_list.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O])
class TaskListFragmentTest {

    @Mock
    lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testEventFragment() {
        val fragmentArgs = Bundle().apply {
            //            putInt("selectedListItem", 0) // ここでbundle指定
        }
//        val factory = // FragmentFactoryわからない・・・（調べる）

        val testList = listOf(
            Task(1, "1gou", false),
            Task(2, "2gou", true),
            Task(3, "3gou", false)
        )
        Mockito.`when`(taskDao.getAll()).thenReturn(testList)
        TaskListFragment.taskDao = taskDao

        val scenario = launchFragmentInContainer<TaskListFragment>(fragmentArgs)
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onFragment {
            val adapter = it.recycler_list.adapter
            if (adapter == null) {
                Assert.fail("adapter is null.")
                return@onFragment
            }

            Assert.assertNotEquals(0, adapter.itemCount)
            Assert.assertEquals(3, adapter.itemCount)

            val view = it.recycler_list.findViewHolderForAdapterPosition(0) as? TaskItemAdapter.ViewHolder
            Assert.assertEquals(false, view?.completedBox?.isChecked)
            Assert.assertEquals("1gou", view?.label?.text)
        }
    }
}