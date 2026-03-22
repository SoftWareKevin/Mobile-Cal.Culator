package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RecordEntry {
    //absolutely not a good name for the class/table but I chose it and if I understand it its fine
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo
    public int date; //uses the unix timestamp
    @ColumnInfo
    public String foodName;
    @ColumnInfo
    public int calories;
    @ColumnInfo
    public int carbs;
    @ColumnInfo
    public int fat;
    @ColumnInfo
    public int protein;

    }


