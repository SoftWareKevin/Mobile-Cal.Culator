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

    @Query("SELECT * FROM RecordEntry WHERE date BETWEEN unixepoch(:date_start) AND unixepoch(:date_end, '-1 second')")
    List<RecordEntry> getRecordsByDate(long date_start, long date_end);



    @Query("SELECT * FROM RecordEntry")
    List<RecordEntry> getAllRecords();

}
