package com.example.fair_division;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeChoiceActivity extends AppCompatActivity {

    int frag = 1;
    FirebaseFirestore firestore;
    AppDatabase db;
    AllocationDao dao;
    SharedPreferences sharedPreferences;
    int lastPushed;
    private final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_choice);

        //INSERT FRAGMENTS HERE
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(navlistener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "allocations")
                .fallbackToDestructiveMigration()
                .build();
        dao = db.allocationDao();
        firestore = FirebaseFirestore.getInstance();
        lastPushed = sharedPreferences.getInt("lastAlloc", 1);

        Log.d("Last Pushed", String.valueOf(lastPushed));

        mDisposable.add(dao.getAllocationsAfter(lastPushed).subscribeOn(Schedulers.io()).subscribe((a) ->
        { if(!a.isEmpty()) {
            handleDbCompleted(a);
        } else {
            Log.d("Empty List", "empty");
        }
            }, Throwable::printStackTrace));


        if (frag == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new GoodsFragment()).commit();
        } else if (frag == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new HomeFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new ChoresFragment()).commit();
        }
        nav.setSelectedItemId(R.id.home_lp);
    }

    private BottomNavigationView.OnItemSelectedListener navlistener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfrag = null;

                    switch (item.getItemId()) {
                        case R.id.goods_lp:
                            selectedfrag = new GoodsFragment();
                            frag = 0;
                            break;
                        case R.id.home_lp:
                            selectedfrag = new HomeFragment();
                            frag = 1;
                            break;
                        case R.id.chores_lp:
                            selectedfrag = new ChoresFragment();
                            frag = 2;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, selectedfrag).commit();
                    return true;
                }

            };

    private void handleDbCompleted(List<Allocation> allocationList) {
        List<String> sessions = dao.getAllocationSessionsSync(lastPushed);
        for(String sess : sessions) {
            Map<String, Object> allocData = new HashMap<>();
            int counter = 1;
            for (Allocation alloc : allocationList) {
                if(!alloc.getSessionId().equals(sess)) {
                    break;
                }
                ArrayList<Object> allocList = new ArrayList<>();
                allocList.add(alloc.getPersonName());
                allocList.add(alloc.getGoodName());
                allocList.add(alloc.getCredits());
                allocData.put("alloc" + counter, allocList);
                counter++;
            }
            firestore.collection("allocations").document("Session " + sess).set(allocData);
        }
        lastPushed = allocationList.get(allocationList.size() - 1).getAllocId() + 1;

        sharedPreferences.edit().putInt("lastAlloc", lastPushed).apply();
    }

}