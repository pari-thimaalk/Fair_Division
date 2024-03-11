package com.example.fair_division;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddCreditsActivity extends AppCompatActivity {

    RecyclerView choicesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credits);
        String name = getIntent().getStringExtra("personName");
        TextView nametext = findViewById(R.id.PersonName);
        nametext.setText(name);

        choicesList = findViewById(R.id.PreferencesList);
        choicesList.setAdapter(new ChoicesAdapter(getIntent().getStringArrayListExtra("goodslist")));
        choicesList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

}
