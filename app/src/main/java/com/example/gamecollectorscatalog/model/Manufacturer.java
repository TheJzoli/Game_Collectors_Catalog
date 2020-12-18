package com.example.gamecollectorscatalog.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Manufacturer {
    @NonNull
    @PrimaryKey
    private String name;

    public Manufacturer(@NonNull String name) { this.name = name; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
