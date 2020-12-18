package com.example.gamecollectorscatalog;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gamecollectorscatalog.model.Console;
import com.example.gamecollectorscatalog.model.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConsolesFragment extends Fragment {

    private static final String ARG_PARAM = "manufacturer";
    private static final String ARG_KEY = "console";

    private NavController navController;
    private RecyclerViewAdapter adapter;
    private ConsoleViewModel consoleViewModel;
    private GameViewModel gameViewModel;

    private List<Game> allGames;
    private String mManufacturer;
    private String newConsole;

    public ConsolesFragment() {
        // Required empty public constructor
    }

    public static ConsolesFragment newInstance(String param1) {
        ConsolesFragment fragment = new ConsolesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mManufacturer = getArguments().getString(ARG_PARAM);
            if (savedInstanceState == null && getArguments().containsKey(ARG_KEY))
                newConsole = getArguments().getString(ARG_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("rotation", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consoles, container, false);
        adapter = new RecyclerViewAdapter(inflater.getContext());
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getAllGames().observe(getViewLifecycleOwner(), games -> {
            allGames = new ArrayList<>();
            if (games != null) {
                allGames.addAll(games);
            }
        });
        consoleViewModel = new ViewModelProvider(requireActivity()).get(ConsoleViewModel.class);
        consoleViewModel.getAllConsoles().observe(getViewLifecycleOwner(), consoles -> {
            List<String> consoleNames = new ArrayList<>();
            if (consoles != null) {
                for (Console c : consoles) {
                    if (c.getManufacturer().equals(mManufacturer))
                        consoleNames.add(c.getName());
                }
            }
            adapter.setData(consoleNames);
        });

        RecyclerView recyclerView = view.findViewById(R.id.ConsolesRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle data = new Bundle();
                data.putString(ARG_KEY, adapter.getItem(position));
                navController.navigate(R.id.action_consolesFragment_to_gamesFragment, data);
            }

            @Override
            public void onLongClick(View view, int position) {
                // Delete console and all games in it.
                for (Game g : allGames) {
                    if (g.getConsole().equals(adapter.getItem(position))) {
                        if (g.getImagePath() != null) {
                            File image = new File(g.getImagePath());
                            if (image.exists()) {
                                boolean imageDeleted = image.delete();
                                Log.d("ConsolesFragment", "Image deleted: " + Boolean.toString(imageDeleted));
                            }
                        }

                    }
                }
                consoleViewModel.delete(new Console(adapter.getItem(position), mManufacturer));
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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_consoles);
        fab.setOnClickListener(thisView -> {
            //Snackbar.make(thisView, "Add console", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM, mManufacturer);
            if (adapter.getAll() != null)
                args.putStringArrayList("allConsoles", new ArrayList<>(adapter.getAll()));
            navController.navigate(R.id.action_consolesFragment_to_addConsoleFragment, args);
        });

        if (newConsole != null) {
            consoleViewModel.insert(new Console(newConsole, mManufacturer));
            newConsole = null;
        }
    }

}