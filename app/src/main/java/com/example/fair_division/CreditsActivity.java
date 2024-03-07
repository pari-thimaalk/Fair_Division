package com.example.fair_division;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class CreditsActivity extends AppCompatActivity {

    RecyclerView creditsPplList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        creditsPplList = findViewById(R.id.creditsPplList);
        creditsPplList.setAdapter(new CreditsPplAdapter(getIntent().getStringArrayListExtra("ppl")));
        creditsPplList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Log.d("Credits People", String.valueOf(getIntent().getStringArrayListExtra("ppl")));
        Log.d("Credits Goods", String.valueOf(getIntent().getStringArrayListExtra("goods")));
    }
}