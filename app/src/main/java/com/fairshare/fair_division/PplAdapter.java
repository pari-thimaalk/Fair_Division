package com.fairshare.fair_division;

import android.app.Dialog;
import android.content.DialogInterface;
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


public class PplAdapter extends RecyclerView.Adapter<PplAdapter.ViewHolder> {
    ArrayList<String> pplList;
    RecyclerView pplListView;
    EditText pplInput;



    public PplAdapter(ArrayList<String> ppl) {
        pplList = ppl;

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        pplListView = recyclerView;
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

        holder.getListHolder().setText(pplList.get(position));

        holder.itemView.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setView(R.layout.dialog_add_person);
//                EditText pplInput;
//            pplInput = view.findViewById(R.id.agent_name_input);

            AlertDialog d = builder.setMessage("Edit the name of the agent").setTitle("Edit Agent").setIcon(R.drawable.ic_baseline_person_24).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pplInput = ((Dialog) dialogInterface).findViewById(R.id.agent_name_input);
                    pplList.remove(holder.getAdapterPosition());
                    pplList.add(holder.getAdapterPosition(), String.valueOf(pplInput.getText()));
                    pplListView.setAdapter(new PplAdapter(pplList));

                }
            }).setNegativeButton("Cancel", ((dialogInterface, i) -> {
            })).create();

//            pplInput = ((Dialog)(d)).findViewById(R.id.agent_name_input);
//            assert pplInput != null;
//            pplInput.setText(pplList.get(holder.getAdapterPosition()));
            d.show();



        });
        
        ImageButton deleteAgent = holder.itemView.findViewById(R.id.deleteAgent);
        deleteAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pplList.remove(holder.getAdapterPosition());
                pplListView.setAdapter(new PplAdapter(pplList));
                Toast.makeText(view.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pplList.size();
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
