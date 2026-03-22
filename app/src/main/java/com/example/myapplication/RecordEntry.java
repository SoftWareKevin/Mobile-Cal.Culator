package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordEntry {
    @PrimaryKey(autoGenerate = true)
    int uid;
    @ColumnInfo
    String foodName;
    @ColumnInfo
    int calories;
    @ColumnInfo
    int carbs;
    @ColumnInfo
    int fat;
    @ColumnInfo
    int protein;

    }


