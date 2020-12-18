package com.example.gamecollectorscatalog;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecollectorscatalog.model.Game;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// temporary fragmant for nintendo consoles
public class GamesFragment extends Fragment {

    private static final String ARG_PARAM = "console";
    private static final String ARG_KEY = "game";

    private NavController navController;
    private GamesRecyclerViewAdapter adapter;
    private GameViewModel gameViewModel;

    private String mConsole;
    private Game newGame;

    public GamesFragment() {
        // Required empty public constructor
    }

    public static GamesFragment newInstance(String param1) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            mConsole = args.getString(ARG_PARAM);
            if (savedInstanceState == null && args.containsKey("gameTitle")) {
                newGame = new Game(args.getString("gameTitle"), mConsole,
                        args.getString("releaseDate"), args.getString("catalogDate"));
                if (args.containsKey("imagePath"))
                    newGame.setImagePath(args.getString("imagePath"));
                if (args.containsKey("notes"))
                    newGame.setNotes(args.getString("notes"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        adapter = new GamesRecyclerViewAdapter(inflater.getContext());
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getAllGames().observe(getViewLifecycleOwner(), games -> {
            List<Integer> gameIds = new ArrayList<>();
            List<String> gameNames = new ArrayList<>();
            List<String> gameImages = new ArrayList<>();
            if (games != null ) {
                for (Game g : games) {
                    if (g.getConsole().equals(mConsole)) {
                        gameNames.add(g.getName());
                        gameIds.add(g.getId());
                        if (g.getImagePath() != null) {
                            gameImages.add(g.getImagePath());
                        } else {
                            gameImages.add("");
                        }

                    }
                }
            }
            adapter.setData(gameNames, gameImages, gameIds);
        });

        RecyclerView recyclerView = view.findViewById(R.id.GamesRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle args = new Bundle();
                args.putInt(ARG_KEY, adapter.getId(position));
                navController.navigate(R.id.action_gamesFragment_to_gameInfoFragment, args);
            }

            @Override
            public void onLongClick(View view, int position) {
                gameViewModel.deleteWithId(adapter.getId(position));
                if (!adapter.getItemImage(position).isEmpty()) {
                    File image = new File(adapter.getItemImage(position));
                    if (image.exists()) {
                        boolean imageDeleted = image.delete();
                        Log.d("GamesFragment", "Image deleted: " + Boolean.toString(imageDeleted));
                    }
                }
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                if (vibrator != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        VibrationEffect effect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
                        vibrator.vibrate(effect);
                    } else {
                        vibrator.vibrate(100);
                    }
                }
                Toast.makeText(getActivity(), "Deleted "+adapter.getItemName(position), Toast.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_games);
        fab.setOnClickListener(thisView -> {
            //Snackbar.make(thisView, "Add game", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM, mConsole);
            navController.navigate(R.id.action_gamesFragment_to_addGameFragment, args);
        });

        if (newGame != null) {
            gameViewModel.insert(newGame);
            newGame = null;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("rotation", true);
    }
}