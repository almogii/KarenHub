package com.example.karenhub.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.karenhub.MyApplication;

@Database(entities = {Post.class}, version = 55)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostsDao PostsDao();

}

public class AppLocalDb{
    static public AppLocalDbRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getMyContext(),
                        AppLocalDbRepository.class,
                        "dbPost.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private AppLocalDb(){}
}

