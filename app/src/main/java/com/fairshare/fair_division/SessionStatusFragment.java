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
        finishedAllocationText = view.findViewById(R.id.finished_allocating_text);
        inSessionText = view.findViewById(R.id.in_session_text);

        firestore.collection("sessions")
                .document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value != null && value.exists()) {
                            ArrayList<String> names = new ArrayList<>();
                            ArrayList<String> ids = new ArrayList<>();
                            HashMap<String, String> users = (HashMap<String, String>) value.get("users");
                            assert users != null;
                            inSessionText.setText("In Session (" + users.size() + "):");


                            for (String key : users.keySet()) {
                                ids.add(key);
                                String name = users.get(key);
                                names.add(name);
                            }

                            if(agentsList.getAdapter() == null) {
                                agentsList.setAdapter(
                                        new AgentStatusAdapter(
                                        names, ids, (String) value.get("owner"),
                                        requireActivity().getIntent().getStringExtra("userId"),
                                SessionStatusFragment.this));
                            } else {
                                ((AgentStatusAdapter)agentsList.getAdapter()).setDataset(names, ids);
                            }

                        }

                    }
                });

    }

    @Override
    public void onUserRemoved(String id) {
        firestore.collection("sessions")
                .document(Objects.requireNonNull(requireActivity().getIntent().getStringExtra("sessionCode")))
                .update("users." + id, FieldValue.delete());
    }
}