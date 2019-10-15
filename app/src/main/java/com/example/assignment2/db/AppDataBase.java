package com.example.assignment2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = Data.class,version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    abstract public DAO Dao();
}
