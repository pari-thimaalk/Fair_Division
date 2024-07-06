package com.fairshare.fair_division;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AgentStatusAdapter extends RecyclerView.Adapter<AgentStatusAdapter.ViewHolder> {
    private ArrayList<String> agentsList, idList;
    private String ownerId, userId;


    public AgentStatusAdapter(ArrayList<String> agents, ArrayList<String> ids, String owner, String currentUser) {
        agentsList = agents;
        ownerId = owner;
        idList = ids;
        userId = currentUser;

    }
    public void setDataset(ArrayList<String> names, ArrayList<String> ids) {
        agentsList = names;
        idList = ids;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getMainText().setText(agentsList.get(position));
        if(idList.get(position).equals(ownerId)) {
            holder.getIcon().setVisibility(View.VISIBLE);
        }
        if(userId.equals(ownerId) && !idList.get(position).equals(userId)) {
            holder.getButton().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return agentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mainText, subText;
        private final ImageView icon;
        private final ImageButton button;
        //TODO: Finish implementing this from Android Docs
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainText = itemView.findViewById(R.id.title_text);
            subText = itemView.findViewById(R.id.description_text);
            icon = itemView.findViewById(R.id.icon_image);
            button = itemView.findViewById(R.id.remove_user_button);
        }

        public TextView getMainText(){
            return mainText;
        }
        public TextView getSubText(){
            return subText;
        }

        public ImageButton getButton() { return button; }

        public ImageView getIcon() {
            return icon;
        }

    }



}
