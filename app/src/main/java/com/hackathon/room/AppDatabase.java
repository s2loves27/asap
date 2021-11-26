package com.hackathon.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hackathon.room.AnnounceCard.AnnounceCard;
import com.hackathon.room.AnnounceCard.AnnounceCardDao;
import com.hackathon.room.User.User;
import com.hackathon.room.User.UserDao;
import com.hackathon.room.worktype.Type;
import com.hackathon.room.worktype.TypeDao;

@Database(entities = {User.class, Type.class, AnnounceCard.class}, version = 1)
@TypeConverters({TypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TypeDao typeDao();
    public abstract AnnounceCardDao announceCardDao();

    private static AppDatabase appDatabase;
    
    //싱글톤 유지
    public static AppDatabase getInstance(Context context){
        if(appDatabase==null){
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "asap").build();
        }
        return appDatabase;
    }

    public static void destroyInstance(){
        appDatabase = null;
    }
    
}
