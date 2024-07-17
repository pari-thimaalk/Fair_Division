package com.fairshare.fair_division;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class HomeChoiceActivity extends AppCompatActivity {

    private Button joinSessionButton, createSessionButton, localSessionButton;
    private FirebaseFirestore firestore;
    private SharedPreferences sharedPreferences;
    private String name, id;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_choice);
        firestore = FirebaseFirestore.getInstance();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                o -> {
                    if(o.getResultCode() == RESULT_CANCELED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeChoiceActivity.this);
// Add the buttons.
                        builder.setTitle("User Removed")
                                .setMessage("You have been kicked from the session.")
                                .setIcon(R.drawable.person_remove_24dp_000000)
                                .setPositiveButton("Ok", (dialog, id) -> {
                                    // User taps OK button.

                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if(o.getResultCode() == RESULT_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeChoiceActivity.this);
// Add the buttons.
                        builder.setTitle("Session Exited")
                                .setMessage("You have left the session successfully.")
                                .setIcon(R.drawable.person_remove_24dp_000000)
                                .setPositiveButton("Ok", (dialog, id) -> {

                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

        joinSessionButton = findViewById(R.id.join_session_button);
        createSessionButton = findViewById(R.id.create_session_button);
        localSessionButton = findViewById(R.id.create_local_button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(sharedPreferences.getString("name", null) == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.dialog_add_person);
            AlertDialog nameDialog = builder.setMessage("Hi! Before we begin, we need a name.")
                    .setTitle("What's Your Name?")
                    .setIcon(R.drawable.ic_baseline_person_24)
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                            String newId = UUID.randomUUID().toString();
                            sharedPreferences.edit().putString("id", newId).apply();
                            id = newId;
                            name = codeInput.getText().toString();

                            sharedPreferences.edit().putString("name", name).apply();

                        }
                    }).create();
            nameDialog.setOnShowListener(dialogInterface -> {
                EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                codeInput.setHint("Your Name");
            });

            nameDialog.show();

        } else {
            id = sharedPreferences.getString("id", null);
            name = sharedPreferences.getString("name", null);
        }



        createSessionButton.setOnClickListener(v -> {
            SessionCreationSheetFragment sessionCreationSheetFragment = new SessionCreationSheetFragment(id, name);
            sessionCreationSheetFragment.show(getSupportFragmentManager(), "SessionCreationSheetFragment");
        });

        localSessionButton.setOnClickListener(v -> {
            SessionCreationSheetFragment sessionCreationSheetFragment = new SessionCreationSheetFragment();
            sessionCreationSheetFragment.show(getSupportFragmentManager(), "LocalSessionCreation");


        });

        joinSessionButton.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeChoiceActivity.this);
            builder.setView(R.layout.dialog_add_person);

            AlertDialog enterCode = builder
                    .setMessage("Input the session code provided by the room owner. It should be a 20-character string.")
                    .setTitle("Enter Session Code")
                    .setIcon(R.drawable.link_24dp)
                    .setPositiveButton("Confirm", (dialogInterface, i) -> {
                        EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                        firestore.collection("sessions")
                                .document(codeInput.getText().toString())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()) {
                                            firestore.collection("sessions").document(codeInput.getText().toString())
                                                    .update("users." + id, name)
                                                    .addOnSuccessListener(unused -> {

                                                        Intent intent = new Intent(HomeChoiceActivity.this, SessionActivity.class);
                                                        intent.putExtra("sessionCode", codeInput.getText().toString());
                                                        intent.putExtra("isOwner", false);
                                                        intent.putExtra("userId", id);
                                                        intent.putStringArrayListExtra("items", (ArrayList<String>) documentSnapshot.get("items"));
                                                        intent.putExtra("type", (Integer) documentSnapshot.get("type"));
                                                        launcher.launch(intent);
                                                    });


                                        } else {
                                            Snackbar.make(v, "The code is invalid!", BaseTransientBottomBar.LENGTH_SHORT).show();

                                        }
                                    }
                                });})
                    .setNegativeButton("Cancel", ((dialogInterface, i) -> {}))
                    .create();

            enterCode.setOnShowListener(dialogInterface -> {
                EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                codeInput.setHint("Session Code");
            });

            enterCode.show();

        });

    }

}