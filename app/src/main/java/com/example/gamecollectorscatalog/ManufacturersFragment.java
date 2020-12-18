package com.example.gamecollectorscatalog;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gamecollectorscatalog.model.Console;
import com.example.gamecollectorscatalog.model.Manufacturer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManufacturersFragment extends Fragment {

    private static final String ARG_KEY = "manufacturer";

    private NavController navController;
    private RecyclerViewAdapter adapter;
    private ManufacturerViewModel manufacturerViewModel;
    private ConsoleViewModel consoleViewModel;
    private String newManufacturer;
    private List<String> allManufacturers;
    private List<Console> allConsoles;

    public ManufacturersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && savedInstanceState == null) {
            newManufacturer = getArguments().getString(ARG_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manufacturers, container, false);

        adapter = new RecyclerViewAdapter(inflater.getContext());
        consoleViewModel = new ViewModelProvider(requireActivity()).get(ConsoleViewModel.class);
        consoleViewModel.getAllConsoles().observe(getViewLifecycleOwner(), consoles -> {
            allConsoles = new ArrayList<>();
            if (consoles != null) {
                allConsoles.addAll(consoles);
            }
        });
        manufacturerViewModel = new ViewModelProvider(requireActivity()).get(ManufacturerViewModel.class);
        manufacturerViewModel.getAllManufacturers().observe(getViewLifecycleOwner(), manufacturers -> {
            List<String> manufacturerNames = new ArrayList<>();
            if (manufacturers != null) {
                for (Manufacturer m : manufacturers)
                    manufacturerNames.add(m.getName());
                allManufacturers = manufacturerNames;
            }
            adapter.setData(manufacturerNames);
        });

        RecyclerView recyclerView = view.findViewById(R.id.ManufacturersRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle data = new Bundle();
                data.putString(ARG_KEY, adapter.getItem(position));
                navController.navigate(R.id.action_manufacturersFragment_to_consolesFragment, data);
            }

            @Override
            public void onLongClick(View view, int position) {
                // Delete all the manufacturer's consoles and their games, and then the manufacturer
                for (Console c : allConsoles) {
                    if (c.getManufacturer().equals(adapter.getItem(position)))
                        consoleViewModel.delete(c);
                }
                manufacturerViewModel.delete(new Manufacturer(adapter.getItem(position)));
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                if (vibrator != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        VibrationEffect effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                        vibrator.vibrate(effect);
                    } else {
                        vibrator.vibrate(100);
                    }
                }
                Toast.makeText(getActivity(), "Deleted "+adapter.getItem(position)+" and everything in it", Toast.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_manufacturers);
        fab.setOnClickListener(thisView -> {
            Bundle args = new Bundle();
            if (allManufacturers != null)
                args.putStringArrayList("allManufacturers", new ArrayList<String>(allManufacturers));
            navController.navigate(R.id.action_manufacturersFragment_to_addManufacturerFragment, args);
        });

        // Add new manufacturer if it was given
        if (newManufacturer != null) {
            manufacturerViewModel.insert(new Manufacturer(newManufacturer));
            newManufacturer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("rotation", true);
    }
}