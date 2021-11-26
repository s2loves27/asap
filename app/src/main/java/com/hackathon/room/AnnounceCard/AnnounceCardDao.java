package com.hackathon.room.AnnounceCard;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AnnounceCardDao {
    @Query("SELECT * FROM announcecard")
    List<AnnounceCard> getALL();

    @Query("SELECT * FROM announcecard WHERE username IN (:username)")
    List<AnnounceCard> getMyAnnounceCard(String username);

    @Query("SELECT * FROM announcecard WHERE username IN (:username) and workFlag = (:workFlag)")
    List<AnnounceCard> getMyAnnounceFlagCard(String username, int workFlag);

    @Insert
    void insert(AnnounceCard announceCard);

    @Delete
    void delete(AnnounceCard announceCard);

    @Update
    void update(AnnounceCard announceCard);
}
