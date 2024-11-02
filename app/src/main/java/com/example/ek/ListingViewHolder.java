package com.example.ek;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListingViewHolder extends RecyclerView.ViewHolder{

    ImageView propertyImage;
    TextView propertyTitle, propertyLocation, propertyPrice, propertyBaths, propertyBeds;

    public ListingViewHolder(@NonNull View itemView) {
        super(itemView);
        propertyImage = itemView.findViewById(R.id.propertyImage);
        propertyTitle = itemView.findViewById(R.id.propertyTitle);
        propertyLocation = itemView.findViewById(R.id.propertyLocation);
        propertyPrice = itemView.findViewById(R.id.propertyPrice);
        propertyBaths = itemView.findViewById(R.id.propertyBaths);
        propertyBeds = itemView.findViewById(R.id.propertyBeds);
    }
}
