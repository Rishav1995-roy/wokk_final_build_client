package com.app.wokk.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app.wokk.adapter.CardAdapterForParticularService;
import com.app.wokk.adapter.ServicesAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.AllCardsResponseModel;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.CardLiistForParticularServiceClass;
import com.app.wokk.model.CardLiistForParticularServiceResponseModel;
import com.app.wokk.model.ServiceClass;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.model.ServiceResponseModelClass;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardListForParticularServiceFragment extends BaseFragment {

    public static CardListForParticularServiceFragment newInstance() {
        return new CardListForParticularServiceFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public TextView tvEmptyText;
    public RecyclerView recyclerView;
    public TextInputEditText etPin;
    public ArrayList<AllCardsResponseModel> cardList;
    public ArrayList<AllCardsResponseModel> filterCardList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView=inflater.inflate(R.layout.fragment_card_for_particular_service,container,false);
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
            getCardListForParticularService();
        }else{
            customAlert(getResources().getString(R.string.noInternetText));
        }
    }

    private void getCardListForParticularService() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        CardLiistForParticularServiceClass cardLiistForParticularServiceClass=new CardLiistForParticularServiceClass();
        cardLiistForParticularServiceClass.apiCredentialModel=apiCredentialModel;
        cardLiistForParticularServiceClass.service_id=myPreference.getServiceId();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(cardLiistForParticularServiceClass);
        Call<CardLiistForParticularServiceResponseModel> getCardListForParticularService= RestManager.getInstance().getService().getCardListForParticularService(jsonElement);
        getCardListForParticularService.enqueue(new Callback<CardLiistForParticularServiceResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<CardLiistForParticularServiceResponseModel> call, @NotNull Response<CardLiistForParticularServiceResponseModel> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code ==1){
                        cardList=new ArrayList<>();
                        filterCardList=new ArrayList<>();
                        cardList.clear();
                        filterCardList.clear();
                        cardList=response.body().all_cards;
                        filterCardData(cardList);
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
            public void onFailure(@NotNull Call<CardLiistForParticularServiceResponseModel> call,@NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private void filterCardData(ArrayList<AllCardsResponseModel> cardList) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = df.format(new Date());
        Long currentTimeStamp = getTimeStampForDate(currentTime);
        for(int i=0;i<cardList.size();i++){
            Long validTime=getTimeStampForDate(cardList.get(i).user_card_valid_until);
            if(validTime>=currentTimeStamp){
                filterCardList.add(cardList.get(i));
            }
        }
        setAdapter(filterCardList,ContainerActivity.layoutUrl);
    }

    private void init(View rootView) {
        tvEmptyText=rootView.findViewById(R.id.tvEmptyText);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        etPin=rootView.findViewById(R.id.etPin);
        if(ContainerActivity.cardDetailsResponseModel != null) {
            ContainerActivity.rlViews.setVisibility(View.VISIBLE);
            ContainerActivity.rlCreate.setVisibility(View.GONE);
        }else{
            ContainerActivity.rlViews.setVisibility(View.GONE);
            ContainerActivity.rlCreate.setVisibility(View.VISIBLE);
        }
        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable charSequence) {
                searchNowList(charSequence.toString());
                if (!charSequence.toString().isEmpty()) {
                    searchNowList(charSequence.toString());
                }
            }
        });
    }

    private void searchNowList(String toString) {
        ArrayList<AllCardsResponseModel> searchList=new ArrayList<>();
        ArrayList<AllCardsResponseModel> filterSearchList=new ArrayList<>();
        searchList.clear();
        filterSearchList.clear();
        for(int i=0;i<cardList.size();i++){
            if(toString.equals("")){
                searchList.add(cardList.get(i));
            }else {
                if (cardList.get(i).user_pin.equals(toString)) {
                    searchList.add(cardList.get(i));
                }
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = df.format(new Date());
        Long currentTimeStamp = getTimeStampForDate(currentTime);
        for(int i=0;i<searchList.size();i++){
            Long validTime=getTimeStampForDate(searchList.get(i).user_card_valid_until);
            if(validTime>=currentTimeStamp){
                filterSearchList.add(searchList.get(i));
            }
        }
        setAdapter(filterSearchList,ContainerActivity.layoutUrl);
    }

    private void setAdapter(ArrayList<AllCardsResponseModel> homelist,String url) {
        if(homelist.size() != 0){
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            CardAdapterForParticularService cardAdapterForParticularService=new CardAdapterForParticularService(this,homelist,url);
            recyclerView.setAdapter(cardAdapterForParticularService);
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

    private Long getTimeStampForDate(String time) {
        Long timeLong;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date parsedDate = simpleDateFormat.parse(time);
            assert parsedDate != null;
            Timestamp timeStamp = new Timestamp(parsedDate.getTime());
            timeLong = timeStamp.getTime();
        }catch (Exception e){
            e.printStackTrace();
            timeLong= Long.valueOf(0);
        }

        return timeLong;
    }

    @Override
    public void onPause() {
        super.onPause();
        etPin.setText("");
        if (etPin.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
}
