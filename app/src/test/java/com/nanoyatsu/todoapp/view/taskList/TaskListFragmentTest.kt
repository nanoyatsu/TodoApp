package com.nanoyatsu.todoapp.view.taskList

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.fragment_task_list.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O])
class TaskListFragmentTest {

    @Before
    fun setUp() {
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
        val scenario = launchFragmentInContainer<TaskListFragment>(fragmentArgs)
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onFragment {
            Assert.assertEquals(it.recycler_list.adapter?.itemCount, 0)
        }
    }
}