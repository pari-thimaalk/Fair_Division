package com.example.fair_division;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        EditText p = holder.itemView.findViewById(R.id.itemq);
        p.setFilters(new InputFilter[]{new QuantityFilter(0,100)});
        holder.itemView.findViewById(R.id.plusfab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur_q = Integer.parseInt(p.getText().toString());
                if(cur_q >= 0 && cur_q < 100)cur_q += 1;
                p.setText(Integer.toString(cur_q));
            }
        });
        holder.itemView.findViewById(R.id.minusfab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur_q = Integer.parseInt(p.getText().toString());
                if(cur_q > 0 && cur_q <= 100)cur_q -= 1;
                p.setText(Integer.toString(cur_q));
            }
        });

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
