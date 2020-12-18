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
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

public class AddManufacturerFragment extends DialogFragment {

    private static final String ARG_KEY = "manufacturer";

    private EditText mEditText;
    private Button addButton;
    private Button cancelButton;

    private List<String> allManufacturers;
    private NavController navController;

    public AddManufacturerFragment() {
        // Empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allManufacturers = new ArrayList<>();
        if (getArguments() != null) {
            if (getArguments().containsKey("allManufacturers")) {
                List<String> manufacturersWithCase = getArguments().getStringArrayList("allManufacturers");
                for (String m : manufacturersWithCase)
                    allManufacturers.add(m.toLowerCase());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_manufacturer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        mEditText = (EditText) view.findViewById(R.id.editText_manufacturer_name);
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        addButton = view.findViewById(R.id.btn_add_manufacturer);
        addButton.setOnClickListener(v -> {
            String newManufacturer = mEditText.getText().toString();
            if (!newManufacturer.isEmpty()) {
                if (!allManufacturers.contains(newManufacturer.toLowerCase())) {
                    Bundle args = new Bundle();
                    args.putString(ARG_KEY, newManufacturer);
                    navController.navigate(R.id.action_addManufacturerFragment_to_manufacturersFragment_add, args);
                } else {
                    Toast.makeText(getActivity(), newManufacturer + " already exists", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Manufacturer name can't be empty", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton = view.findViewById(R.id.btn_cancel_manufacturer);
        cancelButton.setOnClickListener(v -> navController.navigate(R.id.action_addManufacturerFragment_to_manufacturersFragment_cancel));
    }
}
