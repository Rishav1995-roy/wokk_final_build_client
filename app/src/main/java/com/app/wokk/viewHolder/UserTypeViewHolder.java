package com.app.wokk.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;

public class UserTypeViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout llUserType;
    public TextView tvname;
    public View view;
    public View itemView;


    public UserTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        llUserType=itemView.findViewById(R.id.llUserType);
        tvname=itemView.findViewById(R.id.tvname);
        view=itemView.findViewById(R.id.view);
    }
}
