package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecordEntry.class}, version = 1)
public abstract class RecordsDatabase extends RoomDatabase {
    public abstract RecordsDao recordsDao();

}
