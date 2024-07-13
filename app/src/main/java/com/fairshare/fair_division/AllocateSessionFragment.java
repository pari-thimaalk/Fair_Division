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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AllocateSessionFragment extends Fragment implements ChoicesAdapter.AllocationChangedListener {
    private FirebaseFirestore firestore;
    private RecyclerView itemsList;
    private TextView creditsLeft;
    private FloatingActionButton uploadButton;
    private SharedPreferences preferences;
    private HashMap<String, Integer> oldAllocation = new HashMap<>();

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
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        uploadButton = view.findViewById(R.id.upload_allocation_button);
        creditsLeft = view.findViewById(R.id.credits_left);
        itemsList.setAdapter(new ChoicesAdapter(requireActivity().getIntent().getStringArrayListExtra("items"), creditsLeft, this));
        itemsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        firestore = FirebaseFirestore.getInstance();

        uploadButton.setOnClickListener(view1 -> {
                    ChoicesAdapter adapter = (ChoicesAdapter) itemsList.getAdapter();
                    assert adapter != null;
                    int goods_count = adapter.getItemCount();
                    HashMap<String, Integer> alloc = new HashMap<>();
                    for (int i = 0; i < goods_count; i++) {
                        ChoicesAdapter.ViewHolder holder = (ChoicesAdapter.ViewHolder) itemsList.findViewHolderForAdapterPosition(i);
                        assert holder != null;
                        EditText input = holder.itemView.findViewById(R.id.itemq);
                        TextView name = holder.itemView.findViewById((R.id.goodName));
                        alloc.put(name.getText().toString(), Integer.parseInt(input.getText().toString()));
                    }
                    oldAllocation = alloc;

                    DocumentReference ref = firestore.collection("sessions").document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")));
                    ref.update("allocation." + preferences.getString("id", null), alloc)
                            .addOnSuccessListener(unused -> {
                                Snackbar.make(uploadButton, "Allocation successful!", BaseTransientBottomBar.LENGTH_SHORT).show();
                                uploadButton.setVisibility(View.GONE);
                            });

                    ref.update("finished." + preferences.getString("id", null), preferences.getString("name", null))
                            .addOnFailureListener(e -> Log.e("Error in finished update", "here", e));
                    ref.update("users." + preferences.getString("id", null), FieldValue.delete())
                            .addOnFailureListener(e -> Log.e("Error in user update", "here", e));


                });


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

    @Override
    public void onAllocationDone() {
        ChoicesAdapter adapter = (ChoicesAdapter) itemsList.getAdapter();
        assert adapter != null;
        int goods_count = adapter.getItemCount();
        HashMap<String, Integer> alloc = new HashMap<>();
        for (int i = 0; i < goods_count; i++) {
            ChoicesAdapter.ViewHolder holder = (ChoicesAdapter.ViewHolder) itemsList.findViewHolderForAdapterPosition(i);
            assert holder != null;
            EditText input = holder.itemView.findViewById(R.id.itemq);
            TextView name = holder.itemView.findViewById((R.id.goodName));
            alloc.put(name.getText().toString(), Integer.parseInt(input.getText().toString()));
        }
        if(!alloc.equals(oldAllocation)) {
            uploadButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAllocationNotDone() {
        uploadButton.setVisibility(View.GONE);

    }
}