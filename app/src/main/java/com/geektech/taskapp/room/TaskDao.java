package com.geektech.taskapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.geektech.taskapp.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllive();

    @Insert
    void insert(Task task);
    @Update
    void update(Task task);

    @Query("SELECT * FROM task ORDER BY title ASC")
    List<Task> sort();

    @Delete
    void delete(Task task);
}
