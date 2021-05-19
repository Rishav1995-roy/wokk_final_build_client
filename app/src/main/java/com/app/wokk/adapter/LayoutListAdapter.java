package com.app.wokk.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.CardEditActivity;
import com.app.wokk.activity.LayoutSelectActivity;
import com.app.wokk.model.LayoutListModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.viewHolder.LayoutListViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

public class LayoutListAdapter extends RecyclerView.Adapter<LayoutListViewHolder> {

    public LayoutSelectActivity context;
    public ArrayList<LayoutListModel> layoutListModelArrayList;

    public LayoutListAdapter(LayoutSelectActivity layoutSelectFrament, ArrayList<LayoutListModel> layoutListModels) {
        this.context=layoutSelectFrament;
        this.layoutListModelArrayList=layoutListModels;
    }

    @NonNull
    @Override
    public LayoutListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.layout_list_recyler_adapter, null);
        return new LayoutListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LayoutListViewHolder holder, final int position) {
        if(layoutListModelArrayList.get(position).layout != null)
            Glide.with(context).load(layoutListModelArrayList.get(position).layout).into(holder.ivlayout);
        else
            holder.ivlayout.setImageResource(R.drawable.layout1);
        //holder.ivlayout.setImageResource(R.drawable.layout4);
        holder.ivlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreference myPreference=new MyPreference(context);
                myPreference.setLayout(layoutListModelArrayList.get(position).layout);
                myPreference.setLayoutID(layoutListModelArrayList.get(position).layoutId);
                Intent intent = new Intent(context, CardEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Objects.requireNonNull(context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                (context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return layoutListModelArrayList.size();
    }
}
