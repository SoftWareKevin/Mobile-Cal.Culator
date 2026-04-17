package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordsDao {
    @Insert
    void insertEntry(RecordEntry recordEntry);

    @Delete
    void deleteEntry(RecordEntry recordEntry);

    @Query("SELECT * FROM RecordEntry WHERE date >= :date_start AND date <= :date_end")
    List<RecordEntry> getRecordsByDate(long date_start, long date_end);

    @Query("SELECT * FROM RecordEntry")
    List<RecordEntry> getAllRecords();

}