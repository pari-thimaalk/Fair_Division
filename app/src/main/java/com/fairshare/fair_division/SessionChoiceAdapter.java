package com.fairshare.fair_division;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionChoiceAdapter extends RecyclerView.Adapter<SessionChoiceAdapter.ViewHolder> {
    private final SessionCreationSheetFragment bottomSheet;
    private final String id, name;
    private SharedPreferences sharedPreferences;

    public SessionChoiceAdapter(SessionCreationSheetFragment bottomSheet, String id, String name) {
        this.bottomSheet = bottomSheet;
        this.id = id;
        this.name = name;

    }

    private final List<Boolean> items = Arrays.asList(true, false);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 1) {
            holder.getMainText().setText("Allocate Chores");
            holder.getSubText().setText("Unfavorable activities (e.g. expenses, tasks, etc.)");
            holder.getIcon().setImageResource(R.drawable.ic_chores);

        }

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Clicked " + position, Toast.LENGTH_SHORT).show();
            bottomSheet.dismiss();


            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            Map<String, String> users = new HashMap<>();
            users.put(id, name);

            Map<String, Object> data = new HashMap<>();
            data.put("created", new Timestamp(new Date()));
            data.put("allocation", new HashMap<>());
            data.put("users", users); //TODO: Add owner in list to start
            data.put("items", new ArrayList<>());
            data.put("owner", id);
            data.put("type", position);
            data.put("finished", new ArrayList<>());


            firestore.collection("sessions").add(data)
                    .addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(v.getContext(), SessionActivity.class);
                        intent.putExtra("sessionCode", documentReference.getId());
                        intent.putExtra("isOwner", true);
                        intent.putExtra("userId", id);

                        v.getContext().startActivity(intent);

                    });


        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mainText, subText;
        private final ImageView icon;
        //TODO: Finish implementing this from Android Docs
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainText = itemView.findViewById(R.id.title_text);
            subText = itemView.findViewById(R.id.description_text);
            icon = itemView.findViewById(R.id.icon_image);
        }

        public TextView getMainText(){
            return mainText;
        }
        public TextView getSubText(){
            return subText;
        }

        public ImageView getIcon() {
            return icon;
        }

    }

}
