package com.app.wokk.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.fragment.CardListForParticularServiceFragment;
import com.app.wokk.fragment.MycardFragment;
import com.app.wokk.model.AllCardsResponseModel;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.ViewIncreaseModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.app.wokk.viewHolder.CardViewHolderForService;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardAdapterForParticularService extends RecyclerView.Adapter<CardViewHolderForService> {

    CardListForParticularServiceFragment context;
    ArrayList<AllCardsResponseModel> cardList;
    String url, addressColor,emailColor,phoneColor;

    public CardAdapterForParticularService(CardListForParticularServiceFragment cardListForParticularServiceFragment, ArrayList<AllCardsResponseModel> homelist, String url) {
        this.context=cardListForParticularServiceFragment;
        this.cardList=homelist;
        this.url=url;
    }

    @NonNull
    @Override
    public CardViewHolderForService onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.card_for_particular_service_recyler_adapter, null);
        return new CardViewHolderForService(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolderForService holder,final int position) {
        Drawable d=null;
        Glide.with(context).load(url + cardList.get(position).layout_image).into(holder.ivCard);
        if(cardList.get(position).view_count != null)
            holder.tvViewCount.setText(cardList.get(position).view_count);
        else
            holder.tvViewCount.setText("0");
        holder.ivCard.setBackgroundColor(Color.parseColor(cardList.get(position).card_border_color));
        holder.rlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreference myPreference=new MyPreference(context.requireActivity());
                if(myPreference.getUserID().equals(cardList.get(position).card_user_id)){
                    myPreference.setServiceUserID(cardList.get(position).card_user_id);
                    Fragment myCardFragment = MycardFragment.newInstance();
                    FragmentTransaction myCardTransaction = context.requireActivity().getSupportFragmentManager().beginTransaction();
                    myCardTransaction.replace(R.id.frameContainerChild, myCardFragment);
                    myCardTransaction.addToBackStack(null);
                    myCardTransaction.commit();
                }else{
                    boolean networkCheck = NetworkCheck.getInstant(context.requireContext()).isConnectingToInternet();
                    if (networkCheck) {
                        context.showRotateDialog();
                        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
                        apiCredentialModel.apiuser= Constant.apiuser;
                        apiCredentialModel.apipass=Constant.apipass;
                        ViewIncreaseModel viewIncreaseModel=new ViewIncreaseModel();
                        viewIncreaseModel.apiCredentialModel=apiCredentialModel;
                        viewIncreaseModel.user_id=cardList.get(position).card_user_id;
                        Gson gson=new Gson();
                        JsonElement jsonElement=gson.toJsonTree(viewIncreaseModel);
                        Call<ResponseBody> doView= RestManager.getInstance().getService().doViews(jsonElement);
                        doView.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                context.hideRotateDialog();
                                try{
                                    assert response.body() != null;
                                    String val = response.body().string();
                                    JSONObject jsonObject = new JSONObject(val);
                                    if (jsonObject.optInt("code") == 1) {
                                        MyPreference myPreference=new MyPreference(context.requireActivity());
                                        myPreference.setServiceUserID(cardList.get(position).card_user_id);
                                        Fragment myCardFragment = MycardFragment.newInstance();
                                        FragmentTransaction myCardTransaction = context.requireActivity().getSupportFragmentManager().beginTransaction();
                                        myCardTransaction.replace(R.id.frameContainerChild, myCardFragment);
                                        myCardTransaction.addToBackStack(null);
                                        myCardTransaction.commit();
                                    }else if(jsonObject.optInt("code") == 9){
                                        context.customAlert("An authentication error occured!");
                                    }else{
                                        context.customAlert("Oops, something went wrong!");
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    context.customAlert("Oops, something went wrong!");
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                context.hideRotateDialog();
                                context.customAlert("Internal server error. Please try after few minutes.");
                            }
                        });
                    }else{
                        context.customAlert(context.getResources().getString(R.string.noInternetText));
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }
}
