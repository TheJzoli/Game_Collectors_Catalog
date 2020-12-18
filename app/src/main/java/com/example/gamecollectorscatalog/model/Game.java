package com.example.gamecollectorscatalog.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Game {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    private String imagePath;
    @NonNull
    private String console;
    @NonNull
    private String releaseDate;
    @NonNull
    private String catalogDate;

    private String notes;

    public Game(@NonNull String name, @NonNull String console, @NonNull String releaseDate, @NonNull String catalogDate) {
        this.name = name;
        this.console = console;
        this.releaseDate = releaseDate;
        this.catalogDate = catalogDate;
    }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    @NonNull
    public String getName() { return name; }

    public void setName(@NonNull String name) { this.name = name; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @NonNull
    public String getConsole() { return console; }

    public void setConsole(@NonNull String console) { this.console = console; }

    @NonNull
    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(@NonNull String releaseDate) { this.releaseDate = releaseDate; }

    @NonNull
    public String getCatalogDate() { return catalogDate; }

    public void setCatalogDate(@NonNull String catalogDate) { this.catalogDate = catalogDate; }

    public String getNotes() { return notes; }

    public void setNotes(@NonNull String notes) { this.notes = notes; }
}
