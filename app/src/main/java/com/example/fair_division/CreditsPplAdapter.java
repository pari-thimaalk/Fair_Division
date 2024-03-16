package com.example.fair_division;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class CreditsPplAdapter extends RecyclerView.Adapter<CreditsPplAdapter.ViewHolder>{

    ArrayList<String> agents;
    ArrayList<String> goods;
    private Context context;

    public CreditsPplAdapter(ArrayList<String> ppl, ArrayList<String> goods, Context context) {
        agents = ppl;
        this.context = context;
        this.goods = goods;
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
                int position = holder.getAdapterPosition();
                String personName = agents.get(position);
                Intent intent = new Intent(context, AddCreditsActivity.class);
                intent.putExtra("personName",personName);
                intent.putExtra("goodslist",goods);
                intent.putExtra("Personid",position);
                context.startActivity(intent);

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
