package com.example.assignment2.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static java.nio.file.attribute.AclEntryPermission.DELETE;

@Dao
public interface DAO {

    @Query("SELECT*FROM Data")
    List<Data> getAll();

    @Query("SELECT*FROM Data WHERE id=:id")
    Data getDataById(int id);

    @Query("DELETE FROM Data WHERE id = :userId")
    void deleteByUserId(int userId);

    @Query("UPDATE Data SET title=:title,massage=:massage,url=:url WHERE id = :position")
    void updateTest(String title, String massage, Integer position,String url);

    @Query("DELETE FROM Data")
    void deleteAllData();

    @Insert
    void insertAll(Data... data);


    @Delete
    void delete(Data data);

}
