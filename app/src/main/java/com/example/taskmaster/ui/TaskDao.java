package com.example.taskmaster.ui;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskmaster.models.task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task WHERE state != \"COMPLETE\"")
    List<task> getAll();

    @Query("SELECT * FROM task WHERE state = :state")
    List<task> getAllByState(String state);

    @Query("SELECT * FROM task WHERE id = :id")
    task getTaskById(int id);

    @Query("UPDATE task SET state= :state WHERE id = :id")
    void changeState(String state,int id);

    @Insert
    Long insertTask(task task);
}
