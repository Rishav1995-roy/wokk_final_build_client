package com.app.wokk.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.fragment.CardListForParticularServiceFragment;
import com.app.wokk.fragment.MycardFragment;
import com.app.wokk.fragment.ServicesFragment;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.viewHolder.ServicesViewHolder;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesViewHolder>{

    private ServicesFragment context;
    private ArrayList<ServiceListDataModel> servicesmodel;
    private String url;

    public ServicesAdapter(ServicesFragment servicesFragment, ArrayList<ServiceListDataModel> servicesList,String url) {
        this.context=servicesFragment;
        this.servicesmodel=servicesList;
        this.url=url;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.services_recyler_adapter, null);
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder,final int position) {
        if(servicesmodel.get(position).service_image != null) {
            Glide.with(context).load(url + servicesmodel.get(position).service_image).into(holder.ivService);
            //holder.ivService.setImageResource(R.drawable.ambulance); //why this happens I don,t know.
        }
        holder.tvTitle.setText(servicesmodel.get(position).service_name);
        holder.rlService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyPreference myPreference=new MyPreference(context.requireActivity());
                myPreference.setServiceID(servicesmodel.get(position).service_id);
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
        return servicesmodel.size();
    }
}
