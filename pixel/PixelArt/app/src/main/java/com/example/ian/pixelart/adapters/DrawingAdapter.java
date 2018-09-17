package com.example.ian.pixelart.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ian.pixelart.R;
import com.example.ian.pixelart.adapters.viewHolders.DrawingViewHolder;
import com.example.ian.pixelart.models.Drawing;

import java.util.Collections;
import java.util.List;

/**
 * Created by ian.
 */

public class DrawingAdapter extends RecyclerView.Adapter<DrawingViewHolder> {
    List<Drawing> drawings = Collections.emptyList();
    private Context context;

    public DrawingAdapter(List<Drawing>list, Context context){
        this.drawings=list;
        this.context=context;
    }

    @Override
    public DrawingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_row, parent, false);
        DrawingViewHolder holder = new DrawingViewHolder(v);
        Log.d(this.getClass().getSimpleName(), "Creating viewholder");
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawingViewHolder holder, int position) {
        Log.d(this.getClass().getSimpleName(), "Binding position " + position);
        holder.setData(drawings.get(position) );
    }

    @Override
    public int getItemCount() {
        return drawings.size();
    }

    public void insert(int position, Drawing data) {
        drawings.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        drawings.remove(position);
        notifyItemRemoved(position);
    }

    public Drawing getdrawing(int position){
        return  drawings.get(position);
    }

}
