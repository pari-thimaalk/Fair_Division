package com.fairshare.fair_division;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
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
    Integer credits_remaining;
    TextView credits_rm;
    AllocateSessionFragment fragment;

    public Integer getCredits_remaining(){
        return credits_remaining;
    }

    public interface AllocationChangedListener {
        void onAllocationDone();

        void onAllocationNotDone();
    }

    public ChoicesAdapter(ArrayList<String> goods, TextView crm) {
        goodsList = goods;
        credits_remaining = 100;
        credits_rm = crm;
        credits_rm.setText("Credits remaining: "+credits_remaining);
    }

    public ChoicesAdapter(ArrayList<String> goods, TextView crm, AllocateSessionFragment instance) {
        goodsList = goods;
        credits_remaining = 100;
        credits_rm = crm;
        credits_rm.setText("Credits remaining: " + credits_remaining);
        fragment = instance;

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
        EditText itemq = holder.itemView.findViewById(R.id.itemq);
        itemq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    credits_remaining += Integer.parseInt(charSequence.toString());
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0){
                    credits_remaining -= Integer.parseInt(charSequence.toString());
                    Log.d("Credits remaining",String.valueOf(credits_remaining));
                    credits_rm.setText("Credits left: "+ credits_remaining);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(credits_remaining != 0) {
                    fragment.onAllocationNotDone();
                } else {
                    fragment.onAllocationDone();
                }

            }
        });

    }

    public void setDataset(ArrayList<String> newItems) {
        goodsList = newItems;
        notifyDataSetChanged();
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
