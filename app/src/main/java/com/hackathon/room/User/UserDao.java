package com.hackathon.room.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getALL();

    @Query("SELECT * FROM user WHERE email IN (:Email) AND password IN (:Password)")
    List<User> getUserEmail(String Email, String Password);

    @Query("SELECT place FROM user WHERE email IN (:Email)")
    List<String> getUserPlace(String Email);

    @Query("SELECT score FROM user WHERE email IN (:Email)")
    List<Double> getUserScore(String Email);

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);
}
