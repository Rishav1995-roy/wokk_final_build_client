package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class ServicesViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivService;
    public View itemView;
    public RelativeLayout rlService;
    public TextView tvTitle;

    public ServicesViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        ivService=itemView.findViewById(R.id.ivService);
        rlService=itemView.findViewById(R.id.rlService);
        tvTitle=itemView.findViewById(R.id.tvTitle);
    }
}
