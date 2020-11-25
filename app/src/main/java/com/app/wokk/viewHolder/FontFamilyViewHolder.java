package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class FontFamilyViewHolder extends RecyclerView.ViewHolder {

    public View itemView;
    public LinearLayout llUserType;
    public TextView tvname;
    public View view;
    public FontFamilyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        llUserType=itemView.findViewById(R.id.llUserType);
        tvname=itemView.findViewById(R.id.tvname);
        view=itemView.findViewById(R.id.view);
    }
}
