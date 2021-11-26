package com.hackathon.room.worktype;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TypeDao {
    @Query("SELECT * FROM type")
    Type[] getALL();


    @Query("SELECT * FROM type WHERE smalltype LIKE '%' || :smalltype || '%'")
    Type[] getItem(String smalltype);

    @Insert
    void insert(Type type);

    @Delete
    void delete(Type type);
}
