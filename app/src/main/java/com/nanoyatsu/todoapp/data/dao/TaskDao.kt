package com.nanoyatsu.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanoyatsu.todoapp.data.entity.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM Task")
    fun getAllLiveData(): LiveData<List<Task>>

    // たぶんいらない
//    @Query("SELECT * FROM Task WHERE id = :id")
//    fun getById(id: Int): Task

    @Query("SELECT * FROM Task WHERE completed = :completed")
    fun getCompleted(completed: Boolean): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Query("UPDATE Task SET completed = :completed WHERE id = :id")
    fun update(id: Int, completed: Boolean)

    @Query("DELETE FROM Task WHERE id = :id")
    fun deleteById(id: Int): Int

    /**
     * 全件削除
     */
    @Query("DELETE FROM Task")
    fun deleteAll()

    /**
     * completed = true のものを削除
     */
    @Query("DELETE FROM Task WHERE completed = 1")
    fun deleteCompleted(): Int
}