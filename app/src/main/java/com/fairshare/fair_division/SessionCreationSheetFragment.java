package com.fairshare.fair_division;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SessionCreationSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView optionsList;
    private final String id, name;

    public SessionCreationSheetFragment() {
        id = null;
        name = null;
    }


    public SessionCreationSheetFragment(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_session_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize your UI components here

        optionsList = view.findViewById(R.id.options_list);

        optionsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        if(id != null) {
            optionsList.setAdapter(new SessionChoiceAdapter(this, id, name));
        } else {
            optionsList.setAdapter(new SessionChoiceAdapter(this));
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
