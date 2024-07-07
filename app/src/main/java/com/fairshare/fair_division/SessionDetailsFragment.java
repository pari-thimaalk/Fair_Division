package com.fairshare.fair_division;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class SessionDetailsFragment extends Fragment {
    private TextView sessionCode, infoText;

    public SessionDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_session_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionCode = view.findViewById(R.id.session_code_text);
        infoText = view.findViewById(R.id.information_text);
        sessionCode.setText(requireActivity().getIntent().getStringExtra("sessionCode"));
        if(requireActivity().getIntent().getIntExtra("type", -1) == 0) {
            infoText.setText(R.string.goods_info);
        } else if(requireActivity().getIntent().getIntExtra("type", -1) == 1) {
            infoText.setText(R.string.chores_info);

        }

    }
}