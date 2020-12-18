package com.example.gamecollectorscatalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gamecollectorscatalog.model.Game;

import java.util.List;

public class GameViewModel extends AndroidViewModel {
    private DatabaseRepository databaseRepository;
    private LiveData<List<Game>> allGames;

    public GameViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
        allGames = databaseRepository.getAllGames();
    }

    public void insert(Game game) {
        databaseRepository.insert(game);
    }

    public void delete(Game game) {
        databaseRepository.delete(game);
    }

    public void deleteAll() {
        databaseRepository.deleteAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    public void deleteGamesByConsole(String console) { databaseRepository.deleteGamesByConsole(console); }

    public void deleteWithId(int id) { databaseRepository.deleteGameWithId(id); }
}
