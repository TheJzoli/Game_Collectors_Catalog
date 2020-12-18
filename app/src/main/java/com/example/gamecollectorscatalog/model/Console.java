package com.example.gamecollectorscatalog.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Console {
    @NonNull
    @PrimaryKey
    private String name;

    @NonNull
    private String manufacturer;

    public Console(@NonNull String name, @NonNull String manufacturer) {
        this.name = name;
        this.manufacturer = manufacturer;
    }

    @NonNull
    public String getName() { return name; }

    public void setName(@NonNull String name) { this.name = name; }

    @NonNull
    public String getManufacturer() { return manufacturer; }

    public void setManufacturer(@NonNull String manufacturer) { this.manufacturer = manufacturer; }
}
