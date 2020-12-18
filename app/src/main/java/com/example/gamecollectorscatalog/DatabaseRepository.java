package com.example.gamecollectorscatalog;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gamecollectorscatalog.model.Console;
import com.example.gamecollectorscatalog.model.Game;
import com.example.gamecollectorscatalog.model.Manufacturer;

import java.util.List;

public class DatabaseRepository {
    private ManufacturerDao manufacturerDao;
    private ConsoleDao consoleDao;
    private GameDao gameDao;
    private LiveData<List<Manufacturer>> allManufacturers;
    private LiveData<List<Console>> allConsoles;
    private LiveData<List<Game>> allGames;

    public DatabaseRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        manufacturerDao = db.manufacturerDao();
        consoleDao = db.consoleDao();
        gameDao = db.gameDao();
        allManufacturers = manufacturerDao.getAllManufacturers();
        allConsoles = consoleDao.getAllConsoles();
        allGames = gameDao.getAllGames();
    }

    public LiveData<List<Manufacturer>> getAllManufacturers() {
        return allManufacturers;
    }
    public LiveData<List<Console>> getAllConsoles() { return allConsoles; }
    public LiveData<List<Game>> getAllGames() { return allGames; }

    public void insert(Manufacturer m) { new InsertManufacturerAsyncTask(manufacturerDao).execute(m); }
    public void insert(Console c) {
        new InsertConsoleAsyncTask(consoleDao).execute(c);
    }
    public void insert(Game g) { new InsertGameAsyncTask(gameDao).execute(g); }

    public void delete(Manufacturer m) { new DeleteManufacturerAsyncTask(manufacturerDao).execute(m); }
    public void delete(Console c) {
        new DeleteConsoleAsyncTask(consoleDao).execute(c);
    }
    public void delete(Game g) {
        new DeleteGameAsyncTask(gameDao).execute(g);
    }

    public void deleteAllManufacturers() { new DeleteAllManufacturersAsyncTask(manufacturerDao).execute(); }
    public void deleteAllConsoles() {
        new DeleteAllConsolesAsyncTask(consoleDao).execute();
    }
    public void deleteAllGames() {
        new DeleteAllGamesAsyncTask(gameDao).execute();
    }

    public void deleteGamesByConsole(String console) { new DeleteGamesByConsoleAsyncTask(gameDao).execute(console); }
    public void deleteConsolesByManufacturer(String manufacturer) { new DeleteConsolesByManufacturer(consoleDao).execute(manufacturer); }

    public void deleteGameWithId(int id) { new DeleteGameWithIdAsyncTask(gameDao).execute(id); }

    // Manufacturer async tasks
    private static class InsertManufacturerAsyncTask extends AsyncTask<Manufacturer, Void, Void> {
        private ManufacturerDao manufacturerDao;

        private InsertManufacturerAsyncTask(ManufacturerDao manufacturerDao) {
            this.manufacturerDao = manufacturerDao;
        }

        @Override
        protected Void doInBackground(Manufacturer... manufacturers) {
            manufacturerDao.insert(manufacturers[0]);
            return null;
        }
    }
    private static class DeleteManufacturerAsyncTask extends AsyncTask<Manufacturer, Void, Void> {
        private ManufacturerDao manufacturerDao;

        private DeleteManufacturerAsyncTask(ManufacturerDao manufacturerDao) {
            this.manufacturerDao = manufacturerDao;
        }

        @Override
        protected Void doInBackground(Manufacturer... manufacturers) {
            manufacturerDao.delete(manufacturers[0]);
            return null;
        }
    }
    private static class DeleteAllManufacturersAsyncTask extends AsyncTask<Void, Void, Void> {
        private ManufacturerDao manufacturerDao;

        private DeleteAllManufacturersAsyncTask(ManufacturerDao manufacturerDao) {
            this.manufacturerDao = manufacturerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            manufacturerDao.deleteAllManufacturers();
            return null;
        }
    }


    // console async tasks
    private static class InsertConsoleAsyncTask extends AsyncTask<Console, Void, Void> {
        private ConsoleDao consoleDao;

        private InsertConsoleAsyncTask(ConsoleDao consoleDao) {
            this.consoleDao = consoleDao;
        }

        @Override
        protected Void doInBackground(Console... consoles) {
            consoleDao.insert(consoles[0]);
            return null;
        }
    }
    private static class DeleteConsoleAsyncTask extends AsyncTask<Console, Void, Void> {
        private ConsoleDao consoleDao;

        private DeleteConsoleAsyncTask(ConsoleDao consoleDao) {
            this.consoleDao = consoleDao;
        }

        @Override
        protected Void doInBackground(Console... consoles) {
            consoleDao.delete(consoles[0]);
            return null;
        }
    }
    private static class DeleteAllConsolesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ConsoleDao consoleDao;

        private DeleteAllConsolesAsyncTask(ConsoleDao consoleDao) {
            this.consoleDao = consoleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            consoleDao.deleteAllConsoles();
            return null;
        }
    }


    // game async tasks
    private static class InsertGameAsyncTask extends AsyncTask<Game, Void, Void> {
        private GameDao gameDao;

        private InsertGameAsyncTask(GameDao gameDao) { this.gameDao = gameDao; }

        @Override
        protected Void doInBackground(Game... games) {
            gameDao.insert(games[0]);
            return null;
        }
    }
    private static class DeleteGameAsyncTask extends AsyncTask<Game, Void, Void> {
        private GameDao gameDao;

        private DeleteGameAsyncTask(GameDao gameDao) { this.gameDao = gameDao; }

        @Override
        protected Void doInBackground(Game... games) {
            gameDao.delete(games[0]);
            return null;
        }
    }
    private static class DeleteAllGamesAsyncTask extends AsyncTask<Void, Void, Void> {
        private GameDao gameDao;

        private DeleteAllGamesAsyncTask(GameDao gameDao) { this.gameDao = gameDao; }

        @Override
        protected Void doInBackground(Void... voids) {
            gameDao.deleteAllGames();
            return null;
        }
    }
    private static class DeleteGamesByConsoleAsyncTask extends AsyncTask<String, Void, Void> {
        private GameDao gameDao;

        private DeleteGamesByConsoleAsyncTask(GameDao gameDao) { this.gameDao = gameDao; }

        @Override
        protected Void doInBackground(String... consoles) {
            gameDao.deleteGamesByConsole(consoles[0]);
            return null;
        }
    }
    private static class DeleteGameWithIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private GameDao gameDao;

        private DeleteGameWithIdAsyncTask(GameDao gameDao) { this.gameDao = gameDao; }

        @Override
        protected Void doInBackground(Integer... ids) {
            gameDao.deleteWithId(ids[0]);
            return null;
        }
    }
    private static class DeleteConsolesByManufacturer extends AsyncTask<String, Void, Void> {
        private ConsoleDao consoleDao;

        private DeleteConsolesByManufacturer(ConsoleDao consoleDao) { this.consoleDao = consoleDao; }

        @Override
        protected Void doInBackground(String... manufacturers) {
            consoleDao.deleteConsolesByManufacturer(manufacturers[0]);
            return null;
        }
    }
}
