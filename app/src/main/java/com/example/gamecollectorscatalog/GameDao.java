package com.example.gamecollectorscatalog;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gamecollectorscatalog.model.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Insert
    void insert(Game game);

    @Query("SELECT * FROM game")
    LiveData<List<Game>> getAllGames();

    @Delete
    void delete(Game game);

    @Query("DELETE FROM game WHERE id = :id")
    void deleteWithId(int id);

    @Query("DELETE FROM game")
    void deleteAllGames();

    @Query("DELETE FROM game WHERE console = :console")
    void deleteGamesByConsole(String console);
}
