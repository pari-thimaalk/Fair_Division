package com.example.fair_division;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeChoiceActivity extends AppCompatActivity {

    int frag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_choice);

        //INSERT FRAGMENTS HERE
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnItemSelectedListener(navlistener);

        if(frag == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new GoodsFragment()).commit();
        } else if (frag == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new HomeFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, new ChoresFragment()).commit();
        }
    }

    private BottomNavigationView.OnItemSelectedListener navlistener =
            new NavigationBarView.OnItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfrag = null;

                    switch(item.getItemId()) {
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

}