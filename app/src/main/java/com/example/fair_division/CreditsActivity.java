package com.example.fair_division;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class CreditsActivity extends AppCompatActivity {

    RecyclerView creditsPplList;
    static ArrayList<HashMap<String,Integer>> preferenceslog;
    static ArrayList<String> goodsList;
    static ArrayList<String> ppllist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        //if we are coming from addgoods activity, initialize preferences to be empty
        if(getIntent().getIntExtra("isAddGoods",1) == 1){
            //empty dictionary
            int ppl_count = getIntent().getStringArrayListExtra("ppl").size();
            preferenceslog = new ArrayList<>();
            for(int i = 0; i < ppl_count; i++){
                preferenceslog.add((new HashMap<String,Integer>()));
            }
            //assign list of people and goods
            ppllist = getIntent().getStringArrayListExtra("ppl");
            goodsList = getIntent().getStringArrayListExtra("goods");
        } else {
            //get hash from intent
            //index into preferences log and insert hash
            Integer index = getIntent().getIntExtra("id",0);

            preferenceslog.set(index,(HashMap<String, Integer>) getIntent().getSerializableExtra("preferencesHash"));
        }
        creditsPplList = findViewById(R.id.creditsPplList);
        creditsPplList.setAdapter(new CreditsPplAdapter(ppllist,goodsList,CreditsActivity.this));
        creditsPplList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        Log.d("Credits People", String.valueOf(getIntent().getStringArrayListExtra("ppl")));
//        Log.d("Credits Goods", String.valueOf(getIntent().getStringArrayListExtra("goods")));
          Log.d("Preferences Log", String.valueOf(preferenceslog));
    }
}