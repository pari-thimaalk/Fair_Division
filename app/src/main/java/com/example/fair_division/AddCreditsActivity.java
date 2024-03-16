package com.example.fair_division;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCreditsActivity extends AppCompatActivity {

    RecyclerView choicesList;
    Button done_button;
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

        done_button = findViewById(R.id.donechoice1);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get hashmap of preferences
                int goods_count = choicesList.getAdapter().getItemCount();
                HashMap<String,Integer> preferences = new HashMap<>();
                for(int i = 0; i < goods_count; i++) {
                    ChoicesAdapter.ViewHolder holder = (ChoicesAdapter.ViewHolder) choicesList.findViewHolderForAdapterPosition(i);
                    EditText input = (EditText) holder.itemView.findViewById(R.id.itemq);
                    TextView name = (TextView) holder.itemView.findViewById((R.id.goodName));
                    preferences.put(name.getText().toString(), Integer.parseInt(input.getText().toString()));
                }
                Intent intent = new Intent(AddCreditsActivity.this, CreditsActivity.class);
                intent.putExtra("isAddGoods",0);
                intent.putExtra("id",getIntent().getIntExtra("Personid",0));
                intent.putExtra("preferencesHash",preferences);
                startActivity(intent);
            }
        });
    }

}
