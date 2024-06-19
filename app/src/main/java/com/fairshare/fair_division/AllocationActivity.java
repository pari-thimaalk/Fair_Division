package com.fairshare.fair_division;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.ArrayList;

public class AllocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation);
//        TextView mnwAlloc = findViewById(R.id.mnwText);

//        mnwAlloc.setText(getIntent().getStringExtra("mnw"));
        ArrayList<ArrayList<Integer>> resultArray = (ArrayList<ArrayList<Integer>>) getIntent().getBundleExtra("mnwarray").getSerializable("results");
        ArrayList<String> ppllist = getIntent().getStringArrayListExtra("ppllist");
        ArrayList<String> goodslist = getIntent().getStringArrayListExtra("goodslist");

        //convert double integer array into an array list, better for parsing
        //        for (int i = 0; i < resultArray.size(); i++) {
//            ArrayList<Integer> row = new ArrayList<>();
//            for (int j = 0; j < resultArray.get(i).size(); j++) {
//                row.add(resultArray.get(i).get(j));
//            }
//            arrayList2D.add(row);
//        }

        ArrayList<String> allocationlist = new ArrayList<>();
        for(int person = 0; person < resultArray.size();person++){
            String goodstring = ppllist.get(person) + " gets ";
            int num_items = resultArray.get(person).size();
            for(int good = 0; good<num_items; good++){
                if(good>0){ goodstring += " and "; }
                goodstring += goodslist.get(resultArray.get(person).get(good));
            }
            allocationlist.add(goodstring);
        }

        //generate a 2d array where we replace the good numbers with the good names themselves
        ArrayList<String> stringmatrix = new ArrayList<>();
        for(int person = 0; person < resultArray.size();person++){
            String personshare = "";
            int num_items = resultArray.get(person).size();
            for(int good = 0;good < num_items;good++){
                if(good != 0){personshare += "\n";}
                personshare += goodslist.get(resultArray.get(person).get(good));
            }
            stringmatrix.add(personshare);
        }
        Log.d("stirng matrix", String.valueOf(stringmatrix));

//        mnwAlloc.setText(String.valueOf(allocationlist));
        Log.d("allocation matrix",String.valueOf(resultArray));
        Log.d("allocation results", String.valueOf(allocationlist));

//        ArrayAdapter<String> spinneradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,ppllist);
//        Spinner spinner = findViewById(R.id.spinner);
//        spinner.setAdapter(spinneradapter);

        RecyclerView recyclerView = findViewById(R.id.AllocationRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ResultsAdapter(ppllist,stringmatrix));
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("spinner index", String.valueOf(i));
//                recyclerView.setAdapter(new ResultsAdapter(stringmatrix.get(i)));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        //go back to main activity when done
        Button donebtn = findViewById(R.id.donealcbtn);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllocationActivity.this, HomeChoiceActivity.class));
            }
        });
    }


}