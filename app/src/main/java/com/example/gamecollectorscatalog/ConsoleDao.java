package com.example.gamecollectorscatalog;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gamecollectorscatalog.model.Console;
import com.example.gamecollectorscatalog.model.Manufacturer;

import java.util.List;

@Dao
public interface ConsoleDao {
    @Insert
    void insert(Console console);

    @Query("SELECT * FROM console")
    LiveData<List<Console>> getAllConsoles();

    @Delete
    void delete(Console console);

    @Query("DELETE FROM console")
    void deleteAllConsoles();

    @Query("DELETE FROM console WHERE manufacturer = :manufacturer")
    void deleteConsolesByManufacturer(String manufacturer);
}
