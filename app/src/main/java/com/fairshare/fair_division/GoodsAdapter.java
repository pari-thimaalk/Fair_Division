package com.fairshare.fair_division;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    ArrayList<String> goodList;
    RecyclerView goodListView;
    EditText goodInput;



    public GoodsAdapter(ArrayList<String> ppl) {
        goodList = ppl;

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        goodListView = recyclerView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ppl_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getListHolder().setText(goodList.get(position));

        holder.itemView.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setView(R.layout.dialog_add_person);

//                EditText pplInput;
//            pplInput = view.findViewById(R.id.agent_name_input);

            AlertDialog d = builder.setMessage("Edit the name of the good").setTitle("Edit Good")

                    .setIcon(R.drawable.cookie_black_24dp)
                    .setPositiveButton("Confirm", (dialogInterface, i) -> {
                        goodInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);

                        goodList.remove(holder.getAdapterPosition());
                        goodList.add(holder.getAdapterPosition(), String.valueOf(goodInput.getText()));
                        goodListView.setAdapter(new PplAdapter(goodList));

                    }).setNegativeButton("Cancel", ((dialogInterface, i) -> {
            })).create();

//            pplInput = ((Dialog)(d)).findViewById(R.id.agent_name_input);
//            assert pplInput != null;
//            pplInput.setText(pplList.get(holder.getAdapterPosition()));
            d.setOnShowListener(dialogInterface -> {
                goodInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                goodInput.setHint("Good Name");
                goodInput.setText(goodList.get(holder.getAdapterPosition()));

            });
            d.show();


        });

        ImageButton deleteAgent = holder.itemView.findViewById(R.id.deleteAgent);
        deleteAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodList.remove(holder.getAdapterPosition());
                goodListView.setAdapter(new GoodsAdapter(goodList));
                Toast.makeText(view.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return goodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView listHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listHolder = itemView.findViewById(R.id.agentText);
        }

        public TextView getListHolder() {
            return listHolder;
        }
    }

}
