package com.example.ek;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClientProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    Context context;
    List<ProfileItem> items;

    public ClientProfileAdapter(Context context, List<ProfileItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(context).inflate(R.layout.profile_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getItem());
        holder.imageView.setImageResource(items.get(position).getImage());
        holder.arrow.setImageResource(items.get(position).getArrow());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
