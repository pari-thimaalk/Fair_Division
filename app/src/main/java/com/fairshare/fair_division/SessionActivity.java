package com.fairshare.fair_division;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.OnBackPressedCallback;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Objects;

public class SessionActivity extends AppCompatActivity {

    private ActivitySessionBinding binding;
    private FirebaseFirestore firestore;
    private boolean clickedBack = false;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.session_details_text, R.string.session_status_text, R.string.allocate_items_text};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.session_toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        firestore = FirebaseFirestore.getInstance();

        firestore.collection("sessions").document(getIntent().getStringExtra("sessionCode"))
                .addSnapshotListener((value, error) -> {
                    HashMap<String, Object> users = (HashMap<String, Object>) value.get("users");
                    HashMap<String, Object> finished = (HashMap<String, Object>) value.get("finished");
                    assert users != null;
                    assert finished!= null;
                    if(!users.containsKey(getIntent().getStringExtra("userId")) &&
                            !finished.containsKey(getIntent().getStringExtra("userId"))) {

                        if(!clickedBack) {
                            setResult(RESULT_CANCELED);
                            finish();
                        } else {
                            setResult(RESULT_OK);
                            finish();
                        }

                    }
                });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //TODO: Handle removing user from session on pressing back
                AlertDialog.Builder builder = new AlertDialog.Builder(SessionActivity.this);
                // Add the buttons.
                builder.setTitle("Are you sure you want to leave?")
                        .setMessage("You will be removed from the session.")
                        .setIcon(R.drawable.baseline_exit_to_app_24)
                        .setPositiveButton("Confirm", (dialog, id) -> {
                            // User taps OK button.
                            clickedBack = true;
                            String userId = getIntent().getStringExtra("userId");

                            DocumentReference ref = firestore.collection("sessions")
                                    .document(Objects.requireNonNull(getIntent().getStringExtra("sessionCode")));
                            ref.update("users." + userId, FieldValue.delete());
                            ref.update("finished." + userId, FieldValue.delete());
                            ref.update("allocation." + userId, FieldValue.delete());

                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {});

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, (tab, i) -> tab.setText(TAB_TITLES[i])).attach();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SessionActivity.this);
        // Add the buttons.
        builder.setTitle("Are you sure you want to leave?")
                .setMessage("You will be removed from the session.")
                .setIcon(R.drawable.baseline_exit_to_app_24)
                .setPositiveButton("Confirm", (dialog, id) -> {
                    // User taps OK button.
                    clickedBack = true;
                    String userId = getIntent().getStringExtra("userId");
                    DocumentReference ref = firestore.collection("sessions")
                            .document(Objects.requireNonNull(getIntent().getStringExtra("sessionCode")));

                    if(getIntent().getBooleanExtra("isOwner", false)) {
                        //TODO: Remove every user if owner leaves
//                        for()
                    }


                    ref.update("users." + userId, FieldValue.delete());
                    ref.update("finished." + userId, FieldValue.delete());
                    ref.update("allocation." + userId, FieldValue.delete());

                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }
}