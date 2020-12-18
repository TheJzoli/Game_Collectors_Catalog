package com.example.gamecollectorscatalog;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gamecollectorscatalog.model.Manufacturer;

import java.util.List;

@Dao
public interface ManufacturerDao {
    @Insert
    void insert(Manufacturer manufacturer);

    @Query("SELECT * FROM manufacturer")
    LiveData<List<Manufacturer>> getAllManufacturers();

    @Delete
    void delete(Manufacturer manufacturer);

    @Query("DELETE FROM manufacturer")
    void deleteAllManufacturers();

}
