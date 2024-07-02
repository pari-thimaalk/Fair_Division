package com.fairshare.fair_division;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private Button joinSessionButton, createSessionButton;
    private FirebaseFirestore firestore;
    private SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firestore = FirebaseFirestore.getInstance();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        joinSessionButton = view.findViewById(R.id.join_session_button);
        createSessionButton = view.findViewById(R.id.create_session_button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());


        createSessionButton.setOnClickListener(v -> {
            SessionCreationSheetFragment sessionCreationSheetFragment = new SessionCreationSheetFragment();
            sessionCreationSheetFragment.show(getChildFragmentManager(), "SessionCreationSheetFragment");
        });

        joinSessionButton.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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
                                            //TODO: Add user to session
                                            Intent intent = new Intent(requireContext(), SessionActivity.class);
                                            intent.putExtra("sessionCode", codeInput.getText().toString());
                                            intent.putExtra("isOwner", false);
                                            startActivity(intent);


                                        } else {
                                            Snackbar.make(requireView(), "The code is invalid!", BaseTransientBottomBar.LENGTH_SHORT).show();

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