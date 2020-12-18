package com.example.gamecollectorscatalog;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gamecollectorscatalog.model.Console;
import com.example.gamecollectorscatalog.model.Game;
import com.example.gamecollectorscatalog.model.Manufacturer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Database(entities = {Manufacturer.class, Console.class, Game.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ManufacturerDao manufacturerDao();
    public abstract ConsoleDao consoleDao();
    public abstract GameDao gameDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ManufacturerDao manufacturerDao;
        private ConsoleDao consoleDao;
        private GameDao gameDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            manufacturerDao = db.manufacturerDao();
            consoleDao = db.consoleDao();
            gameDao = db.gameDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Manufacturer> manufacturers = Arrays.asList(
                    new Manufacturer("Nintendo"),
                    new Manufacturer("Sony"),
                    new Manufacturer("Microsoft"),
                    new Manufacturer("Sega"),
                    new Manufacturer("Atari"),
                    new Manufacturer("Other")
            );
            for (Manufacturer m : manufacturers)
                manufacturerDao.insert(m);
            List<Console> consoles = Arrays.asList(
                    // Nintendo consoles
                    new Console("NES", "Nintendo"),
                    new Console("Game Boy", "Nintendo"),
                    new Console("SNES", "Nintendo"),
                    new Console("Virtual Boy", "Nintendo"),
                    new Console("Nintendo 64", "Nintendo"),
                    new Console("Game Boy Color", "Nintendo"),
                    new Console("Game Boy Advance", "Nintendo"),
                    new Console("Nintendo GameCube", "Nintendo"),
                    new Console("Nintendo DS", "Nintendo"),
                    new Console("Wii", "Nintendo"),
                    new Console("Nintendo 3DS", "Nintendo"),
                    new Console("Wii U", "Nintendo"),
                    new Console("Nintendo Switch", "Nintendo"),
                    // Sony consoles
                    new Console("PlayStation", "Sony"),
                    new Console("PlayStation 2", "Sony"),
                    new Console("PlayStation Portable", "Sony"),
                    new Console("PlayStation 3", "Sony"),
                    new Console("PlayStation Vita", "Sony"),
                    new Console("PlayStation 4", "Sony"),
                    new Console("PlayStation 5", "Sony"),
                    // Microsoft consoles
                    new Console("Xbox", "Microsoft"),
                    new Console("Xbox 360", "Microsoft"),
                    new Console("Xbox One", "Microsoft"),
                    new Console("Xbox Series X/Series S", "Microsoft"),
                    // Sega consoles
                    new Console("Master System", "Sega"),
                    new Console("Sega Genesis", "Sega"),
                    new Console("Sega Game Gear", "Sega"),
                    new Console("Sega Saturn", "Sega"),
                    new Console("Dreamcast", "Sega"),
                    // Atari Consoles
                    new Console("Atari 2600", "Atari"),
                    new Console("Atari 5200", "Atari"),
                    new Console("Atari Lynx", "Atari"),
                    // Misc. / other platforms
                    new Console("PC", "Other"),
                    new Console("Android", "Other"),
                    new Console("iOS", "Other")
            );
            for (Console c : consoles)
                consoleDao.insert(c);
            List<Game> games = Arrays.asList(
                    // TODO Temporary games to see that they display correctly
                    new Game("Example Game 1", "NES", "01.01.2020", "18.12.2020"),
                    new Game("Example Game 2", "NES", "10.10.2020", "18.12.2020")
            );
            for (Game g : games) {
                g.setNotes("This is an area for any notes you would want to write about the game.");
                gameDao.insert(g);
            }
            return null;
        }
    }
}
