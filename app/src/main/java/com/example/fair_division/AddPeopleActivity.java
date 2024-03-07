package com.example.fair_division;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class AddPeopleActivity extends AppCompatActivity {
    Button fab;
    Button nextBtn;
    RecyclerView pplList;
    EditText agentInput;
    TextView noAgents;
    ArrayList<String> agents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        fab = findViewById(R.id.fab);
        noAgents = findViewById(R.id.noAgentsText);
        pplList = findViewById(R.id.agentsList);
        nextBtn = findViewById(R.id.nextBtn);


        noAgents.setVisibility(View.VISIBLE);

        fab.setOnClickListener((view) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(AddPeopleActivity.this);
            builder.setView(getLayoutInflater().inflate(R.layout.dialog_add_person, null));
            builder.setMessage("Add the name of the new agent").setTitle("Add Agent").setIcon(R.drawable.ic_baseline_person_24).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    agentInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                    agents.add(String.valueOf(agentInput.getText()));
                    pplList.setAdapter(new PplAdapter(agents));
                    pplList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    noAgents.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.VISIBLE);


                }
            }).setNegativeButton("Cancel", ((dialogInterface, i) -> {
            })).create().show();
        });

        nextBtn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddGoodsActivity.class);
            i.putStringArrayListExtra("ppl", agents);
            startActivity(i);
        });
    }
}