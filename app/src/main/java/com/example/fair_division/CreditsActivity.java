package com.example.fair_division;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        Log.d("Credits People", String.valueOf(getIntent().getStringArrayListExtra("ppl")));
        Log.d("Credits Goods", String.valueOf(getIntent().getStringArrayListExtra("goods")));
    }
}