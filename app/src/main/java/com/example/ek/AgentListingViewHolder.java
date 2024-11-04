package com.example.ek;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AgentListingViewHolder extends RecyclerView.ViewHolder{

    ImageView propertyImage;
    TextView propertyTitle, propertyLocation, propertyPrice, propertyBaths, propertyBeds;

    public AgentListingViewHolder(@NonNull View itemView) {
        super(itemView);
        propertyImage = itemView.findViewById(R.id.propertyImageA);
        propertyTitle = itemView.findViewById(R.id.propertyTitleA);
        propertyLocation = itemView.findViewById(R.id.propertyLocationA);
        propertyPrice = itemView.findViewById(R.id.propertyPriceA);
        propertyBaths = itemView.findViewById(R.id.propertyBathsA);
        propertyBeds = itemView.findViewById(R.id.propertyBedsA);
    }
}
