package com.example.karenhub.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostsDao {
    @Query("select * from Post")
    List<Post> getAll();

    @Query("select * from Post where id = :postId")
    Post getPostById(String postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Post... posts);

    @Delete
    void delete(Post post);
}

