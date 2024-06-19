package com.fairshare.fair_division;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class CreditsPplAdapter extends RecyclerView.Adapter<CreditsPplAdapter.ViewHolder>{

    ArrayList<String> agents;
    ArrayList<String> goods;
    private Context context;
    ArrayList<HashMap<String, Integer>> data;
    ImageView alloc, notAlloc;

    public CreditsPplAdapter(ArrayList<String> ppl, ArrayList<String> goods, Context context, ArrayList<HashMap<String, Integer>> data) {
        agents = ppl;
        this.context = context;
        this.goods = goods;
        this.data = data;
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
        if(!data.get(position).isEmpty()) {
            notAlloc = holder.itemView.findViewById(R.id.notAllocated);
            alloc = holder.itemView.findViewById(R.id.allocated);
            notAlloc.setVisibility(View.GONE);
            alloc.setVisibility(View.VISIBLE);

        }

        holder.itemView.setOnClickListener(view -> {
            //TODO: Add the new activity to start here
            int position1 = holder.getAdapterPosition();
            String personName = agents.get(position1);
            Intent intent = new Intent(context, AddCreditsActivity.class);
            intent.putExtra("personName",personName);
            intent.putExtra("goodslist",goods);
            intent.putExtra("Personid", position1);
            context.startActivity(intent);

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
