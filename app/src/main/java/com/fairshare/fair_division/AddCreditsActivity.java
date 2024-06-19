package com.fairshare.fair_division;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class AddCreditsActivity extends AppCompatActivity {

    RecyclerView choicesList;
    Button done_button;
    TextView creditsrm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credits);
        String name = getIntent().getStringExtra("personName");
        TextView nametext = findViewById(R.id.PersonName);
        nametext.setText(name);
        creditsrm = findViewById(R.id.CreditsLeft);
        choicesList = findViewById(R.id.PreferencesList);
        choicesList.setAdapter(new ChoicesAdapter(getIntent().getStringArrayListExtra("goodslist"),creditsrm));
        choicesList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        done_button = findViewById(R.id.donechoice1);
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoicesAdapter adp= (ChoicesAdapter) choicesList.getAdapter();
                assert adp != null;
                if(adp.getCredits_remaining() != 0){
                    Snackbar.make(view, "You need to allocate all your credits!", Snackbar.LENGTH_LONG).show();
                }else{
                    //get hashmap of preferences
                    int goods_count = adp.getItemCount();
                    HashMap<String,Integer> preferences = new HashMap<>();
                    for(int i = 0; i < goods_count; i++) {
                        ChoicesAdapter.ViewHolder holder = (ChoicesAdapter.ViewHolder) choicesList.findViewHolderForAdapterPosition(i);
                        assert holder != null;
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
            }
        });
    }

}
