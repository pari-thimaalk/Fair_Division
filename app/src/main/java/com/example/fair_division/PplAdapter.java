package com.example.fair_division;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PplAdapter extends RecyclerView.Adapter<PplAdapter.ViewHolder> {
    ArrayList<String> pplList;


    public PplAdapter(ArrayList<String> ppl) {
        pplList = ppl;

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
