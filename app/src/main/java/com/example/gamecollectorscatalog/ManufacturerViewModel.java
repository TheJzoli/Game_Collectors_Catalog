package com.example.gamecollectorscatalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gamecollectorscatalog.model.Manufacturer;

import java.util.List;

public class ManufacturerViewModel extends AndroidViewModel {
    private DatabaseRepository databaseRepository;
    private LiveData<List<Manufacturer>> allManufacturers;

    public ManufacturerViewModel(@NonNull Application application) {
        super(application);
        databaseRepository = new DatabaseRepository(application);
        allManufacturers = databaseRepository.getAllManufacturers();
    }

    public void insert(Manufacturer manufacturer) {
        databaseRepository.insert(manufacturer);
    }

    public void delete(Manufacturer manufacturer) {
        databaseRepository.delete(manufacturer);
    }

    public void deleteAll() {
        databaseRepository.deleteAllManufacturers();
    }

    public LiveData<List<Manufacturer>> getAllManufacturers() {
        return allManufacturers;
    }
}
