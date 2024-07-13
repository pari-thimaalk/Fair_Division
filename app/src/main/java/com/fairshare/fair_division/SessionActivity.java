package com.fairshare.fair_division;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fairshare.fair_division.ui.main.SectionsPagerAdapter;
import com.fairshare.fair_division.databinding.ActivitySessionBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class SessionActivity extends AppCompatActivity {

    private ActivitySessionBinding binding;
    private FirebaseFirestore firestore;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.session_details_text, R.string.session_status_text, R.string.allocate_items_text};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("sessions").document(getIntent().getStringExtra("sessionCode"))
                .addSnapshotListener((value, error) -> {
                    HashMap<String, Object> users = (HashMap<String, Object>) value.get("users");
                    HashMap<String, Object> finished = (HashMap<String, Object>) value.get("finished");
                    assert users != null;
                    assert finished!= null;
                    if(!users.containsKey(getIntent().getStringExtra("userId")) &&
                            !finished.containsKey(getIntent().getStringExtra("userId"))) {

                        setResult(RESULT_CANCELED);
                        finish();

                    }
                });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, (tab, i) -> tab.setText(TAB_TITLES[i])).attach();

    }



}