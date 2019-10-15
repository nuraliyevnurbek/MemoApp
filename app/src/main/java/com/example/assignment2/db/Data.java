package com.example.assignment2.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Data")
public class Data {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "massage")
    public String massage;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "url")
    public String url;
}
