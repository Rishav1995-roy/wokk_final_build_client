package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class HomeRecylerViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivItemPic;
    public View itemView;
    public TextView tvTitle;

    public HomeRecylerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        ivItemPic=itemView.findViewById(R.id.ivItemPic);
        tvTitle=itemView.findViewById(R.id.tvTitle);
    }
}
