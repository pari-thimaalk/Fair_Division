package com.fairshare.fair_division;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddItemsActivity extends AppCompatActivity {
    private FloatingActionButton addItemsButton;
    private FirebaseFirestore firestore;
    private RecyclerView itemsList;
    private final ArrayList<String> items = new ArrayList<>();
    private MenuItem confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        firestore = FirebaseFirestore.getInstance();
        itemsList = findViewById(R.id.goodsList);
        itemsList.setAdapter(new GoodsAdapter(new ArrayList<>()));
        itemsList.setLayoutManager(new LinearLayoutManager(this));
        addItemsButton = findViewById(R.id.add_item_button);
        addItemsButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setView(R.layout.dialog_add_person);

            AlertDialog enterCode = builder
                    .setMessage("Add an item that needs to be allocated.")
                    .setIcon(R.drawable.cookie_black_24dp)
                    .setPositiveButton("Confirm", (dialogInterface, i) -> {
                        EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                        items.add(codeInput.getText().toString());
                        itemsList.setAdapter(new GoodsAdapter(items, confirmButton));
                        })
                    .setNegativeButton("Cancel", ((dialogInterface, i) -> {}))
                    .create();

            enterCode.setOnShowListener(dialogInterface -> {
                EditText codeInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                codeInput.setHint("Item Name");
            });

            enterCode.show();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_items_app_bar_menu, menu);
        confirmButton = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.confirm_items_button) {
            String id = getIntent().getStringExtra("id");
            String name = getIntent().getStringExtra("name");
            int position = getIntent().getIntExtra("type", 0);
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            Map<String, String> users = new HashMap<>();
            users.put(id, name);

            Map<String, Object> data = new HashMap<>();
            data.put("created", new Timestamp(new Date()));
            data.put("allocation", new HashMap<>());
            data.put("users", users); //TODO: Add owner in list to start
            data.put("items", items);
            data.put("owner", id);
            data.put("type", position);
            data.put("finished", new HashMap<>());


            firestore.collection("sessions").add(data)
                    .addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(this, SessionActivity.class);
                        intent.putExtra("sessionCode", documentReference.getId());
                        intent.putExtra("isOwner", true);
                        intent.putExtra("userId", id);
                        intent.putExtra("type", position);
                        intent.putStringArrayListExtra("items", items);


                        startActivity(intent);
                        finish();

                    });

        }
        return super.onOptionsItemSelected(item);
    }
}