package com.example.fair_division;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AllocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation);
        TextView mnwAlloc = findViewById(R.id.mnwText);

        mnwAlloc.setText(getIntent().getStringExtra("mnw"));
        int[][] resultarray = (int[][]) getIntent().getSerializableExtra("mnwarray");
        ArrayList<String> ppllist = getIntent().getStringArrayListExtra("ppllist");
        ArrayList<String> goodslist = getIntent().getStringArrayListExtra("goodslist");

        ArrayList<ArrayList<Integer>> arrayList2D = new ArrayList<>();
        for (int i = 0; i < resultarray.length; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < resultarray[i].length; j++) {
                row.add(resultarray[i][j]);
            }
            arrayList2D.add(row);
        }

        ArrayList<String> allocationlist = new ArrayList<>();
        for(int person = 0; person < resultarray.length;person++){
            String goodstring = ppllist.get(person) + " gets ";
            int num_items = arrayList2D.get(person).size();
            for(int good = 0; good<num_items; good++){
                if(good>0){ goodstring += " and "; }
                goodstring += goodslist.get(arrayList2D.get(person).get(good));
            }
            allocationlist.add(goodstring);
        }
        mnwAlloc.setText(String.valueOf(allocationlist));
        Log.d("allocation matrix",String.valueOf(arrayList2D));
        Log.d("allocation results", String.valueOf(allocationlist));



        Button donebtn = findViewById(R.id.donealcbtn);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllocationActivity.this, HomeChoiceActivity.class));
            }
        });
    }
}