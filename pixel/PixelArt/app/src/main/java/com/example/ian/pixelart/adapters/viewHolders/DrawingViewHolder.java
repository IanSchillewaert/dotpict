package com.example.ian.pixelart.adapters.viewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.ian.pixelart.R;
import com.example.ian.pixelart.models.Drawing;

/**
 * Created by ian.
 */

public class DrawingViewHolder extends ViewHolder {
    private CardView cv;
    private TextView name;

    public DrawingViewHolder(View itemView){
        super(itemView);
        this.cv = itemView.findViewById(R.id.cardView);
        this.name = itemView.findViewById(R.id.loadTitle);
    }

    public void setData( Drawing drawing){
        name.setText(drawing.getName());
    }
}
