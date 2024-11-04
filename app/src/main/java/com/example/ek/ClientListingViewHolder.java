package com.example.ek;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClientListingViewHolder extends RecyclerView.ViewHolder{

    ImageView propertyImage;
    TextView propertyTitle, propertyLocation, propertyPrice, propertyBaths, propertyBeds;

    public ClientListingViewHolder(@NonNull View itemView) {
        super(itemView);
        propertyImage = itemView.findViewById(R.id.propertyImageC);
        propertyTitle = itemView.findViewById(R.id.propertyTitleC);
        propertyLocation = itemView.findViewById(R.id.propertyLocationC);
        propertyPrice = itemView.findViewById(R.id.propertyPriceC);
        propertyBaths = itemView.findViewById(R.id.propertyBathsC);
        propertyBeds = itemView.findViewById(R.id.propertyBedsC);
    }
}
