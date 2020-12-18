package com.example.gamecollectorscatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

public class AddConsoleFragment extends DialogFragment {

    private static final String ARG_PARAM = "manufacturer";
    private static final String ARG_KEY = "console";

    private EditText mEditText;
    private Button addButton;
    private Button cancelButton;

    private String mManufacturer;
    private List<String> allConsoles;
    private NavController navController;

    public AddConsoleFragment() {
        // Empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allConsoles = new ArrayList<>();
        if (getArguments() != null) {
            mManufacturer = getArguments().getString(ARG_PARAM);
            if (getArguments().containsKey("allConsoles")) {
                List<String> consolesWithCase = getArguments().getStringArrayList("allConsoles");
                for (String c : consolesWithCase)
                    allConsoles.add(c.toLowerCase());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_console, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        mEditText = (EditText) view.findViewById(R.id.editText_console_name);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addButton = view.findViewById(R.id.btn_add_console);
        addButton.setOnClickListener(v -> {
            String newConsole = mEditText.getText().toString();
            if (!newConsole.isEmpty()) {
                if (!allConsoles.contains(newConsole.toLowerCase())) {
                    Bundle args = new Bundle();
                    args.putString(ARG_KEY, newConsole);
                    args.putString(ARG_PARAM, mManufacturer);
                    navController.navigate(R.id.action_addConsoleFragment_to_consolesFragment_add, args);
                } else {
                    Toast.makeText(getActivity(), newConsole + " already exists", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Console name can't be empty", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton = view.findViewById(R.id.btn_cancel_console);
        cancelButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(ARG_PARAM, mManufacturer);
            navController.navigate(R.id.action_addConsoleFragment_to_consolesFragment_cancel, args);
        });
    }
}
