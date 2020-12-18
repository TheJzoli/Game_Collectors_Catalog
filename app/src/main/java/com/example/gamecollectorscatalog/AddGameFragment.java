package com.example.gamecollectorscatalog;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddGameFragment extends DialogFragment {

    private static final String ARG_PARAM = "console";
    private static final int CAMERA_PERMISSION_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;

    private String currentPhotoPath;
    private String imageFileName;

    private EditText mGameTitle;
    private EditText mReleaseDate;
    private EditText mCatalogDate;
    private ImageButton takePictureButton;
    private TextView mImageNameText;
    private EditText mImageNameField;
    private EditText mNotes;
    private Button addButton;
    private Button cancelButton;

    private final Calendar myCalendar = Calendar.getInstance();

    private String mConsole;
    private NavController navController;

    public AddGameFragment() {
        // Empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConsole = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        mGameTitle = view.findViewById(R.id.editText_game_title);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mReleaseDate = view.findViewById(R.id.editText_release_date);
        mCatalogDate = view.findViewById(R.id.editText_catalog_date);
        // Add today's date automatically to catalog date section
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(myCalendar.getTime());
        mCatalogDate.setText(timeStamp);


        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mReleaseDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(myCalendar.getTime()));
        };
        mReleaseDate.setOnClickListener(v -> new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        mImageNameField = view.findViewById(R.id.image_name_field);
        mImageNameText = view.findViewById(R.id.textView_image_name);
        mNotes = view.findViewById(R.id.editText_notes);

        takePictureButton = view.findViewById(R.id.btn_take_picture);
        takePictureButton.setOnClickListener(v -> {
            askCameraPermission();
        });

        addButton = view.findViewById(R.id.btn_add_game);
        addButton.setOnClickListener(v -> {
            String newGameTitle = mGameTitle.getText().toString();
            String newReleaseDate = mReleaseDate.getText().toString();
            String newCatalogDate = mCatalogDate.getText().toString();
            String newNotes = mNotes.getText().toString();
            if (!newGameTitle.isEmpty()) {
                if (!newReleaseDate.isEmpty()) {
                    if (!newCatalogDate.isEmpty()) {
                        Bundle args = new Bundle();
                        args.putString("gameTitle", newGameTitle);
                        args.putString("releaseDate", newReleaseDate);
                        args.putString("catalogDate", newCatalogDate);
                        if (currentPhotoPath != null)
                            args.putString("imagePath", currentPhotoPath);
                        if (!newNotes.isEmpty())
                            args.putString("notes", newNotes);
                        args.putString(ARG_PARAM, mConsole);
                        navController.navigate(R.id.action_addGameFragment_to_gamesFragment_add, args);
                    } else {
                        Toast.makeText(getActivity(), "Catalog date can't be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Release date can't be empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Game title can't be empty", Toast.LENGTH_SHORT).show();
            }
        });
        cancelButton = view.findViewById(R.id.btn_cancel_game);
        cancelButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(ARG_PARAM, mConsole);
            navController.navigate(R.id.action_addGameFragment_to_gamesFragment_cancel, args);
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("imageName")) {
                if (savedInstanceState.getString("imageName") != null) {
                    imageFileName = savedInstanceState.getString("imageName");
                    mImageNameField.setText(imageFileName);
                    mImageNameText.setVisibility(View.VISIBLE);
                    mImageNameField.setVisibility(View.VISIBLE);
                }
            }
            if (savedInstanceState.containsKey("imagePath"))
                currentPhotoPath = savedInstanceState.getString("imagePath");
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required to take a picture.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mImageNameText.setVisibility(View.VISIBLE);
                mImageNameField.setVisibility(View.VISIBLE);
                mImageNameField.setText(imageFileName+".jpg");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("AddGameFragment", "Error occurred while creating the File", ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(getActivity(), "An error occurred while saving the image", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("imageName", imageFileName);
        outState.putString("imagePath", currentPhotoPath);
    }
}
