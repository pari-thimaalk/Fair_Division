package com.example.fair_division;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddGoodsActivity extends AppCompatActivity {
    Button fab;
    Button nextBtn;
    RecyclerView goodsList;
    EditText goodsInput;
    TextView noGoods;
    ArrayList<String> goods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
//        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        fab = findViewById(R.id.goodsFab);
        noGoods = findViewById(R.id.noGoodsText);
        goodsList = findViewById(R.id.goodsList);
        nextBtn = findViewById(R.id.goodsNextBtn);

        noGoods.setVisibility(View.VISIBLE);

        fab.setOnClickListener((view) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(AddGoodsActivity.this);
            builder.setView(getLayoutInflater().inflate(R.layout.dialog_add_good, null));
            builder.setMessage("Add the name of the new good").setTitle("Add Good").setIcon(R.drawable.cookie_black_24dp).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    goodsInput = ((Dialog) dialogInterface).findViewById(R.id.good_name_input);
                    goods.add(String.valueOf(goodsInput.getText()));
                    goodsList.setAdapter(new PplAdapter(goods));
                    goodsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    noGoods.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.VISIBLE);

                }
            }).setNegativeButton("Cancel", ((dialogInterface, i) -> {
            })).create().show();
        });

        nextBtn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), CreditsActivity.class);
            i.putStringArrayListExtra("ppl", getIntent().getStringArrayListExtra("ppl"));
            i.putStringArrayListExtra("goods", goods);
            startActivity(i);
        });
    }
}