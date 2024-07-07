package com.fairshare.fair_division;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ChoresActivity extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ImageButton fab;
    Button nextBtn;
    Button backBtn;
    RecyclerView goodsList;
    EditText goodsInput;
    TextView noGoods;
    ArrayList<String> goods = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        TextView tv = findViewById(R.id.managegoods);
        tv.setText("Manage Chores");
        EditText et = findViewById(R.id.addGoodInput);
        et.setHint("Add New Chore");
        TextView tv1 = findViewById(R.id.noGoodsText);
        tv1.setText("No Chores found");
        fab = findViewById(R.id.goodsFab);
        noGoods = findViewById(R.id.noGoodsText);
        goodsList = findViewById(R.id.goodsList);
        nextBtn = findViewById(R.id.goodsNextBtn);
        goodsInput = findViewById(R.id.addGoodInput);

        noGoods.setVisibility(View.VISIBLE);

        fab.setOnClickListener(view -> {
            if(goodsInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Add a new chore's name!", Toast.LENGTH_SHORT).show();
            } else {
                for(String chore : goods) {
                    if(chore.equalsIgnoreCase(goodsInput.getText().toString().trim())) {
                        Toast.makeText(this, "This chore is already in the list!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                goods.add(goodsInput.getText().toString().trim());
                goodsList.setAdapter(new ChoresAdapter(goods));
                goodsList.setLayoutManager(new LinearLayoutManager(this));
                noGoods.setVisibility(View.GONE);
                if(goods.size() >=2) {
                    nextBtn.setVisibility(View.VISIBLE);
                }
                goodsInput.setText("");
            }
        });


        nextBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, AddPeopleActivity.class);
            i.putStringArrayListExtra("goods", goods);
//            i.putExtra("isAddGoods",1);
            i.putExtra("isgood",0);
            startActivity(i);
        });
    }
}