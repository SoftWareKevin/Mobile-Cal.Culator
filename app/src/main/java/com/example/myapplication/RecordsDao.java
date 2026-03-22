package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordsDao {
    @Insert
    void insert(RecordEntry recordEntry);

    @Delete
    void delete(RecordEntry recordEntry);

    @Query("SELECT * FROM RecordEntry")
    List<RecordEntry> getAllRecords();

}
