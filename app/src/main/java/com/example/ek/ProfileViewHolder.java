package com.example.ek;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView, arrow;
    TextView textView;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_profile);
        textView = itemView.findViewById(R.id.tv_profile);
        arrow = itemView.findViewById(R.id.iv_arrow);
    }
}
