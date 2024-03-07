package com.example.fair_division;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.view.Window;
import android.widget.ScrollView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // Scrolled down, perform transition to the next page
                    // Example: startActivity(new Intent(MainActivity.this, NextActivity.class));
                    startActivity(new Intent(MainActivity.this, HomeChoiceActivity.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        });
    }
}