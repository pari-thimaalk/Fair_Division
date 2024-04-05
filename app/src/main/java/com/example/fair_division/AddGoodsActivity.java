package com.example.fair_division;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddGoodsActivity extends AppCompatActivity {
    ImageButton fab;
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
        goodsInput = findViewById(R.id.addGoodInput);

        noGoods.setVisibility(View.VISIBLE);

        fab.setOnClickListener(view -> {
            if(goodsInput.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Add a new agent's name!", Toast.LENGTH_SHORT).show();
            } else {
                goods.add(goodsInput.getText().toString());
                goodsList.setAdapter(new PplAdapter(goods));
                goodsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                noGoods.setVisibility(View.GONE);
                if(goods.size() >=2) {
                    nextBtn.setVisibility(View.VISIBLE);
                }
                goodsInput.setText("");
            }
        });


        nextBtn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), CreditsActivity.class);
            i.putStringArrayListExtra("ppl", getIntent().getStringArrayListExtra("ppl"));
            i.putStringArrayListExtra("goods", goods);
            i.putExtra("isAddGoods",1);
            startActivity(i);
        });
    }
}