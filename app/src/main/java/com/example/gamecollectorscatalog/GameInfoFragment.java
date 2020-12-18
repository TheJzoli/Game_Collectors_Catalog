package com.example.gamecollectorscatalog;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gamecollectorscatalog.model.Game;

import java.io.File;

public class GameInfoFragment extends Fragment {

    private static final String DEBUG_TAG = "GameInfoFragment";
    private static final String ARG_KEY = "game";

    private GameViewModel gameViewModel;

    private String mConsole;
    private int mGameId;
    private Game game;

    private TextView gameTitle;
    private ImageView image;
    private TextView releaseDate;
    private TextView catalogDate;
    private TextView notes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameId = getArguments().getInt(ARG_KEY);
            Log.d(DEBUG_TAG, "Got id "+mGameId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_info, container, false);

        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getAllGames().observe(getViewLifecycleOwner(), games -> {
            if (games != null) {
                for (Game g : games) {
                    if (g.getId() == mGameId) {
                        game = g;
                        populateView(game);
                    }
                }
            }
        });

        gameTitle = view.findViewById(R.id.gameInfo_title);
        image = view.findViewById(R.id.gameInfo_image);
        releaseDate = view.findViewById(R.id.gameInfo_releaseDate);
        catalogDate = view.findViewById(R.id.gameInfo_catalogDate);
        notes = view.findViewById(R.id.gameInfo_notes);

        return view;
    }

    private void populateView(Game game) {
        if (game != null) {
            gameTitle.setText(game.getName());
            if (game.getImagePath() != null) {
                File img = new File(game.getImagePath());
                if (img.exists())
                    image.setImageURI(Uri.fromFile(img));
            }
            releaseDate.setText(game.getReleaseDate());
            catalogDate.setText(game.getCatalogDate());
            if (game.getNotes() != null)
                notes.setText(game.getNotes());
        } else {
            gameTitle.setText("Error Fetching Game");
        }
    }
}
