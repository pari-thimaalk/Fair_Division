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

public class AddPeopleActivity extends AppCompatActivity {
    ImageButton fab;
    Button nextBtn;
    Button backBtn;
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
        backBtn = findViewById(R.id.backBtn);
        agentInput = findViewById(R.id.addAgentInput);


        fab.setOnClickListener(view -> {
            if(agentInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Add a new agent's name!", Toast.LENGTH_SHORT).show();
            } else {
                for(String agent : agents) {
                    if(agent.equalsIgnoreCase(agentInput.getText().toString().trim())) {
                        Toast.makeText(this, "This agent is already in the list!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                agents.add(agentInput.getText().toString());
                pplList.setAdapter(new PplAdapter(agents));
                pplList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                noAgents.setVisibility(View.GONE);

                backBtn.setVisibility(View.VISIBLE);

                if(agents.size() >=2) {
                    nextBtn.setVisibility(View.VISIBLE);
                }
                agentInput.setText("");
            }

        });


        nextBtn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), CreditsActivity.class);
            i.putStringArrayListExtra("ppl", agents);
            i.putExtra("isgood", getIntent().getIntExtra("isgood",1));
            i.putStringArrayListExtra("goods", getIntent().getStringArrayListExtra("goods"));
            startActivity(i);
        });

        backBtn.setOnClickListener(view -> {
            finish();
        });

    }
}