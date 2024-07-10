package com.fairshare.fair_division;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

public class AllocateSessionFragment extends Fragment {
    private FirebaseFirestore firestore;
    private RecyclerView itemsList;
    private TextView creditsLeft;
    private ArrayList<String> items = new ArrayList<>();

    public AllocateSessionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_allocate_session, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsList = view.findViewById(R.id.items_list);
        creditsLeft = view.findViewById(R.id.credits_left);
        itemsList.setAdapter(new ChoicesAdapter(requireActivity().getIntent().getStringArrayListExtra("items"), creditsLeft));
        itemsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        firestore = FirebaseFirestore.getInstance();


//        firestore.collection("sessions")
//                .document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")))
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if(value != null && value.exists()) {
//
//                            ((ChoicesAdapter) Objects.requireNonNull(itemsList.getAdapter())).setDataset((ArrayList<String>) value.get("items"));
//
//                        }
//
//                    }
//                });

//        addItemButton.setOnClickListener((v) -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//            builder.setView(R.layout.dialog_add_person);
//
//            AlertDialog enterCode = builder
//                    .setMessage("Add an item that needs to be allocated.")
//                    .setIcon(R.drawable.cookie_black_24dp)
//                    .setPositiveButton("Confirm", (dialogInterface, i) -> {
//                        EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
//                        items.add(codeInput.getText().toString());
//
//                        firestore.collection("sessions")
//                                .document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")))
//                                .update("items", items)
//
//                                ;})
//                    .setNegativeButton("Cancel", ((dialogInterface, i) -> {}))
//                    .create();
//
//            enterCode.setOnShowListener(dialogInterface -> {
//                EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
//                codeInput.setHint("Item Name");
//            });
//
//            enterCode.show();
//        });

    }
}