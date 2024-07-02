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


    public SessionCreationSheetFragment() {
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
        optionsList.setAdapter(new SessionChoiceAdapter(this));
        optionsList.setLayoutManager(new LinearLayoutManager(requireContext()));

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
