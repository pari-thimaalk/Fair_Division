package com.fairshare.fair_division;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class SessionStatusFragment extends Fragment implements AgentStatusAdapter.UserRemovedListener {

    private FirebaseFirestore firestore;
    private RecyclerView agentsList;
    private TextView inSessionText, finishedAllocationText;



    public SessionStatusFragment() {
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
        firestore = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_session_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        agentsList = view.findViewById(R.id.agentsList);
        agentsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        inSessionText = view.findViewById(R.id.in_session_text);

        firestore.collection("sessions")
                .document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null && value.exists()) {
                            ArrayList<String> names = new ArrayList<>();
                            ArrayList<String> ids = new ArrayList<>();
                            ArrayList<Boolean> areFinished = new ArrayList<>();
                            HashMap<String, String> users = (HashMap<String, String>) value.get("users");
                            HashMap<String, String> finished = (HashMap<String, String>) value.get("finished");

                            assert users != null;
                            assert finished != null;

                            inSessionText.setText("In Session (" + (users.size() + finished.size()) + "):");

                            for(String key : finished.keySet()) {
                                ids.add(key);
                                String name = finished.get(key);
                                names.add(name);
                                areFinished.add(true);
                            }


                            for (String key : users.keySet()) {
                                ids.add(key);
                                String name = users.get(key);
                                names.add(name);
                                areFinished.add(false);
                            }

                            if(agentsList.getAdapter() == null) {
                                agentsList.setAdapter(
                                        new AgentStatusAdapter(
                                        names, ids, areFinished, (String) value.get("owner"),
                                        requireActivity().getIntent().getStringExtra("userId"),
                                SessionStatusFragment.this));
                            } else {
                                ((AgentStatusAdapter)agentsList.getAdapter()).setDataset(names, ids, areFinished);
                            }

                        }

                    }
                });

    }

    @Override
    public void onUserRemoved(String id) {
        DocumentReference ref = firestore.collection("sessions").document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")));
        ref.update("users." + id, FieldValue.delete());
        ref.update("finished." + id, FieldValue.delete());
        ref.update("allocation." + id, FieldValue.delete());

    }
}