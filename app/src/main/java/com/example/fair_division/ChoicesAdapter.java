package com.example.fair_division;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.ViewHolder> {
    ArrayList<String> goodsList;
    public ChoicesAdapter(ArrayList<String> goods) {
        goodsList = goods;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choices_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getListHolder().setText(goodsList.get(position));
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView listHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listHolder = itemView.findViewById(R.id.goodName);
        }

        public TextView getListHolder() {
            return listHolder;
        }
    }
}
