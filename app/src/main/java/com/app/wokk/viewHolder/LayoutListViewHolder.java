package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class LayoutListViewHolder extends RecyclerView.ViewHolder {

    public View itemView;
    public ImageView ivlayout;

    public LayoutListViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        ivlayout=itemView.findViewById(R.id.ivlayout);
    }
}
