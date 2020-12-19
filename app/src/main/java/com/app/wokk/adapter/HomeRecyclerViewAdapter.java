package com.app.wokk.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.fragment.CardListForParticularServiceFragment;
import com.app.wokk.fragment.HomeFragment;
import com.app.wokk.fragment.MycardFragment;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.viewHolder.HomeRecylerViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecylerViewHolder> {
     public HomeFragment context;
     public ArrayList<ServiceListDataModel> homeModelList;
     public String url;

    public HomeRecyclerViewAdapter(HomeFragment homeFragment, ArrayList<ServiceListDataModel> homelist,String url) {
        this.context=homeFragment;
        this.homeModelList=homelist;
        this.url=url;
    }

    @NonNull
    @Override
    public HomeRecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.home_recyler_adapter, null);
        return new HomeRecylerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecylerViewHolder holder, final int position) {
        if(homeModelList.get(position).service_image != null){
            Glide.with(context).load(url+homeModelList.get(position).service_image).into(holder.ivItemPic);
        }
        holder.tvTitle.setText(homeModelList.get(position).service_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreference myPreference=new MyPreference(context.requireActivity());
                myPreference.setServiceID(homeModelList.get(position).service_id);
                Fragment myCardFragment = CardListForParticularServiceFragment.newInstance();
                FragmentTransaction myCardTransaction = context.requireActivity().getSupportFragmentManager().beginTransaction();
                myCardTransaction.replace(R.id.frameContainerChild, myCardFragment);
                myCardTransaction.addToBackStack(null);
                myCardTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeModelList.size();
    }
}
