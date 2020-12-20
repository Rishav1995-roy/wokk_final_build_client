package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class CardViewHolderForService extends RecyclerView.ViewHolder {

    public View itemView;
    public TextView tvViewCount;
    public ImageView ivCard;
    public RelativeLayout rlCard;
    
    public CardViewHolderForService(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        tvViewCount=itemView.findViewById(R.id.tvViewCount);
        ivCard=itemView.findViewById(R.id.ivCard);
        rlCard=itemView.findViewById(R.id.rlCard);
    }
}
