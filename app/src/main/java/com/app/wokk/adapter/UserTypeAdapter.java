package com.app.wokk.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.fragment.CreateCardFragment;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.viewHolder.UserTypeViewHolder;

import java.util.ArrayList;

public class UserTypeAdapter extends RecyclerView.Adapter<UserTypeViewHolder>{

    public ArrayList<ServiceListDataModel> userTypeModelList;
    public CreateCardFragment context;
    public TextView tvUserType;
    public ImageView ivDropdown;
    public RelativeLayout rlView;

    public UserTypeAdapter(CreateCardFragment userDetailsFragment, ArrayList<ServiceListDataModel> userTypeList, TextView tvUserType, ImageView ivDropdown, RelativeLayout rlRecyclerView) {
        this.userTypeModelList=userTypeList;
        this.context=userDetailsFragment;
        this.tvUserType=tvUserType;
        this.ivDropdown=ivDropdown;
        this.rlView=rlRecyclerView;
    }

    @NonNull
    @Override
    public UserTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.user_type_recyler_adapter, null);
        return new UserTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTypeViewHolder holder, final int position) {
        holder.tvname.setText(userTypeModelList.get(position).service_name);
        if(position == (userTypeModelList.size()-1)){
            holder.view.setVisibility(View.GONE);
        }else{
            holder.view.setVisibility(View.VISIBLE);
        }
        holder.llUserType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvUserType.setText(userTypeModelList.get(position).service_name);
                ivDropdown.setTag("down");
                ivDropdown.setImageResource(R.drawable.down);
                rlView.setVisibility(View.GONE);
                CreateCardFragment.serviceID=userTypeModelList.get(position).service_id;
                CreateCardFragment.userTypeSelected=true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return userTypeModelList.size();
    }
}
