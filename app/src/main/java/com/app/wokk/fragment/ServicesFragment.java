package com.app.wokk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.adapter.ServicesAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.ServiceClass;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.model.ServiceResponseModelClass;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.app.wokk.utility.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesFragment extends BaseFragment {

    public View rootView;
    public MyPreference myPreference;
    public TextView tvEmptyText;
    public RecyclerView recyclerView;
    public ArrayList<ServiceListDataModel> servicesList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView=inflater.inflate(R.layout.fragment_services,container,false);
        //ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
        //ContainerActivity.rlMyCardViews.setVisibility(View.GONE);
        myPreference=new MyPreference(getActivity());
        init(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean networkCheck = NetworkCheck.getInstant(getActivity()).isConnectingToInternet();
        if (networkCheck) {
            getServiceList();
        }else{
            customAlert(getResources().getString(R.string.noInternetText));
        }
    }

    private void getServiceList() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        ServiceClass serviceClass=new ServiceClass();
        serviceClass.apiCredentialModel=apiCredentialModel;
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(serviceClass);
        Call<ServiceResponseModelClass> getService= RestManager.getInstance().getService().get_service(jsonElement);
        getService.enqueue(new Callback<ServiceResponseModelClass>() {
            @Override
            public void onResponse(@NotNull Call<ServiceResponseModelClass> call, @NotNull Response<ServiceResponseModelClass> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code ==1){
                        servicesList=new ArrayList<>();
                        servicesList.clear();
                        servicesList=response.body().data;
                        setHomeAdapter(servicesList,response.body().url);
                    }else if(code == 9){
                        customAlert("An authentication error occured!");
                    }else{
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e){
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ServiceResponseModelClass> call,@NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private void init(View rootView) {
        tvEmptyText=rootView.findViewById(R.id.tvEmptyText);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        if(ContainerActivity.cardDetailsResponseModel != null) {
            ContainerActivity.rlViews.setVisibility(View.VISIBLE);
            ContainerActivity.rlCreate.setVisibility(View.GONE);
        }else{
            ContainerActivity.rlViews.setVisibility(View.GONE);
            ContainerActivity.rlCreate.setVisibility(View.VISIBLE);
        }
    }

    private void setHomeAdapter(ArrayList<ServiceListDataModel> homelist,String url) {
        if(homelist.size() != 0){
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ServicesAdapter servicesAdapter=new ServicesAdapter(this,homelist,url);
            recyclerView.setAdapter(servicesAdapter);
        }else{
            recyclerView.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
        if(!customAlertWithOneButton.isShowing()){
            customAlertWithOneButton.show();
            customAlertWithOneButton.tvDesc.setText(s);
            customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithOneButton.dismiss();
                }
            });
        }
    }


    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }
}
