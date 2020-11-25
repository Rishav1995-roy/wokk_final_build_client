package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class LayoutViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivlayout;
    public ImageView ivCheckBox;
    public View itemView;
    public RelativeLayout rlLayout;

    public LayoutViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        ivlayout=itemView.findViewById(R.id.ivlayout);
        ivCheckBox=itemView.findViewById(R.id.ivCheckBox);
        rlLayout=itemView.findViewById(R.id.rlLayout);
    }
}
