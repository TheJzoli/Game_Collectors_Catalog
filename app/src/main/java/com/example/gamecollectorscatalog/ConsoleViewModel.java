package com.example.gamecollectorscatalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gamecollectorscatalog.model.Console;
import com.example.gamecollectorscatalog.model.Manufacturer;

import java.util.List;

public class ConsoleViewModel extends AndroidViewModel {
    private DatabaseRepository databaseRepository;
    private LiveData<List<Console>> allConsoles;

    public ConsoleViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
        allConsoles = databaseRepository.getAllConsoles();
    }

    public void insert(Console console) {
        databaseRepository.insert(console);
    }

    public void delete(Console console) {
        databaseRepository.delete(console);
        databaseRepository.deleteGamesByConsole(console.getName());
    }

    public void deleteAll() {
        databaseRepository.deleteAllConsoles();
    }

    public LiveData<List<Console>> getAllConsoles() {
        return allConsoles;
    }
}
