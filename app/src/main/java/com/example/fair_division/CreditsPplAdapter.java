package com.example.fair_division;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CreditsPplAdapter extends RecyclerView.Adapter<CreditsPplAdapter.ViewHolder>{

    ArrayList<String> agents;

    public CreditsPplAdapter(ArrayList<String> ppl) {
        agents = ppl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credits_ppl_adapter, parent, false);

        return new CreditsPplAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditsPplAdapter.ViewHolder holder, int position) {
        holder.getListHolder().setText(agents.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add the new activity to start here
            }
        });

    }

    @Override
    public int getItemCount() {
        return agents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView listHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listHolder = itemView.findViewById(R.id.creditsAgentText);
        }

        public TextView getListHolder() {
            return listHolder;
        }
    }
}
