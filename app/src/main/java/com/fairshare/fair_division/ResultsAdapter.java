package com.fairshare.fair_division;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private ArrayList<String> People;
    private ArrayList<String> ItemShare;

    // Constructor to set data
    public ResultsAdapter(ArrayList<String> People1, ArrayList<String> isare) {
        People = People1;
        ItemShare = isare;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_adapter, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameView.setText(People.get(position));
        holder.gsview.setText(ItemShare.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return People.size();
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView gsview;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.Person);
            gsview = itemView.findViewById(R.id.GoodShare);
        }
    }
}
