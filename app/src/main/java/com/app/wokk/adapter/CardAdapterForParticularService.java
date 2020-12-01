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
        /*try {
            d=new LoadBackground(url + cardList.get(position).layout_image, "androidfigure").execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        holder.rlCard.setBackgroundDrawable(d);*/
        /*GradientDrawable gd = new GradientDrawable();
        //gd.setColor(Color.RED);
        //gd.setCornerRadius(10);
        gd.setStroke(10, Color.parseColor(cardList.get(position).card_border_color));*/
        holder.ivCard.setBackgroundColor(Color.parseColor(cardList.get(position).card_border_color));
        if(cardList.get(position).user_fname != null && cardList.get(position).user_lname != null)
            holder.tvName.setText(cardList.get(position).user_fname+" "+cardList.get(position).user_lname);
        if(cardList.get(position).user_address != null && cardList.get(position).user_pin != null)
            holder.tvAddress.setText(cardList.get(position).user_address+" - "+ cardList.get(position).user_pin);
        if(cardList.get(position).user_phone != null)
            holder.tvphoneNumber.setText(cardList.get(position).user_phone);
        if(cardList.get(position).user_organization_name != null)
            holder.tvOrganisationName.setText(cardList.get(position).user_organization_name);
        if(cardList.get(position).user_email != null)
            holder.tvemailAddress.setText(cardList.get(position).user_email);
        RelativeLayout.LayoutParams tvNameParams = (RelativeLayout.LayoutParams) holder.tvName.getLayoutParams();
        if(cardList.get(position).card_name_top_mob != null)
            tvNameParams.topMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_name_top_mob));
        else
            tvNameParams.topMargin=0;
        if(cardList.get(position).card_name_left_mob != null)
            tvNameParams.leftMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_name_left_mob));
        else
            tvNameParams.leftMargin=0;
        holder.tvName.setLayoutParams(tvNameParams);
        RelativeLayout.LayoutParams tvOrganisationParams = (RelativeLayout.LayoutParams) holder.tvOrganisationName.getLayoutParams();
        if(cardList.get(position).card_org_top_mob != null)
            tvOrganisationParams.topMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_org_top_mob));
        else
            tvOrganisationParams.topMargin=0;
        if(cardList.get(position).card_org_left_mob != null)
            tvOrganisationParams.leftMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_org_left_mob));
        else
            tvOrganisationParams.leftMargin=0;
        holder.tvOrganisationName.setLayoutParams(tvOrganisationParams);
        RelativeLayout.LayoutParams addressParams = (RelativeLayout.LayoutParams) holder.llAddress.getLayoutParams();
        if(cardList.get(position).card_address_top_mob != null)
            addressParams.topMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_address_top_mob));
        else
            addressParams.topMargin=0;
        if(cardList.get(position).card_address_left_mob != null)
            addressParams.leftMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_address_left_mob));
        else
            addressParams.leftMargin=0;
        holder.llAddress.setLayoutParams(addressParams);
        RelativeLayout.LayoutParams phoneParams = (RelativeLayout.LayoutParams) holder.llPhoneNumber.getLayoutParams();
        if(cardList.get(position).card_phone_top_mob != null)
            phoneParams.topMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_phone_top_mob));
        else
            phoneParams.topMargin=0;
        if(cardList.get(position).card_phone_left_mob != null)
            phoneParams.leftMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_phone_left_mob));
        else
            phoneParams.leftMargin=0;
        holder.llPhoneNumber.setLayoutParams(phoneParams);
        RelativeLayout.LayoutParams mailParams = (RelativeLayout.LayoutParams) holder.llMail.getLayoutParams();
        if(cardList.get(position).card_email_top_mob != null)
            mailParams.topMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_email_top_mob));
        else
            mailParams.topMargin=0;
        if(cardList.get(position).card_email_left_mob != null)
            mailParams.leftMargin=Integer.parseInt(String.valueOf(cardList.get(position).card_email_left_mob));
        else
            mailParams.leftMargin=0;
        holder.llMail.setLayoutParams(mailParams);
        if(cardList.get(position).card_org_color.contains("0") && cardList.get(position).card_org_color.length() == 4){
            holder.tvOrganisationName.setTextColor(Color.parseColor(cardList.get(position).card_org_color+"000"));
        }else if(cardList.get(position).card_org_color.toLowerCase().contains("f") && cardList.get(position).card_org_color.length() == 4){
            holder.tvOrganisationName.setTextColor(Color.parseColor(cardList.get(position).card_org_color.toLowerCase()+"fff"));
        }else{
            holder.tvOrganisationName.setTextColor(Color.parseColor(cardList.get(position).card_org_color));
        }
        if(cardList.get(position).card_name_color.contains("0") && cardList.get(position).card_name_color.length() == 4){
            holder.tvName.setTextColor(Color.parseColor(cardList.get(position).card_name_color+"000"));
        }else if(cardList.get(position).card_name_color.toLowerCase().contains("f") && cardList.get(position).card_name_color.length() == 4){
            holder.tvName.setTextColor(Color.parseColor(cardList.get(position).card_name_color.toLowerCase()+"fff"));
        }else{
            holder.tvName.setTextColor(Color.parseColor(cardList.get(position).card_name_color));
        }
        if(cardList.get(position).card_address_color.contains("0") && cardList.get(position).card_address_color.length() == 4){
            holder.tvAddress.setTextColor(Color.parseColor(cardList.get(position).card_address_color+"000"));
            addressColor=cardList.get(position).card_address_color+"000";
        }else if(cardList.get(position).card_address_color.toLowerCase().contains("f") && cardList.get(position).card_address_color.length() == 4){
            holder.tvAddress.setTextColor(Color.parseColor(cardList.get(position).card_address_color.toLowerCase()+"fff"));
            addressColor=cardList.get(position).card_address_color+"fff";
        }else{
            holder.tvAddress.setTextColor(Color.parseColor(cardList.get(position).card_address_color));
            addressColor=cardList.get(position).card_address_color;
        }
        ImageViewCompat.setImageTintMode(holder.ivAddress, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(holder.ivAddress, ColorStateList.valueOf(Color.parseColor(addressColor)));
        if(cardList.get(position).card_email_color.contains("0") && cardList.get(position).card_email_color.length() == 4){
            holder.tvemailAddress.setTextColor(Color.parseColor(cardList.get(position).card_email_color+"000"));
            emailColor=cardList.get(position).card_email_color+"000";
        }else if(cardList.get(position).card_email_color.toLowerCase().contains("f") && cardList.get(position).card_email_color.length() == 4){
            holder.tvemailAddress.setTextColor(Color.parseColor(cardList.get(position).card_email_color.toLowerCase()+"fff"));
            emailColor=cardList.get(position).card_email_color+"fff";
        }else{
            holder.tvemailAddress.setTextColor(Color.parseColor(cardList.get(position).card_email_color));
            emailColor=cardList.get(position).card_email_color;
        }
        ImageViewCompat.setImageTintMode(holder.ivMail, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(holder.ivMail, ColorStateList.valueOf(Color.parseColor(emailColor)));
        if(cardList.get(position).card_phone_color.contains("0") && cardList.get(position).card_phone_color.length() == 4){
            holder.tvphoneNumber.setTextColor(Color.parseColor(cardList.get(position).card_phone_color+"000"));
            phoneColor=cardList.get(position).card_phone_color+"000";
        }else if(cardList.get(position).card_phone_color.toLowerCase().contains("f") && cardList.get(position).card_phone_color.length() == 4){
            holder.tvphoneNumber.setTextColor(Color.parseColor(cardList.get(position).card_phone_color.toLowerCase()+"fff"));
            phoneColor=cardList.get(position).card_phone_color+"fff";
        }else{
            holder.tvphoneNumber.setTextColor(Color.parseColor(cardList.get(position).card_phone_color));
            phoneColor=cardList.get(position).card_phone_color;
        }
        ImageViewCompat.setImageTintMode(holder.ivPhone, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(holder.ivPhone, ColorStateList.valueOf(Color.parseColor(phoneColor)));
        if(cardList.get(position).card_org_show.equals("0")){
            holder.tvOrganisationName.setVisibility(View.GONE);
        }else{
            holder.tvOrganisationName.setVisibility(View.VISIBLE);
        }
        if(cardList.get(position).card_name_show.equals("0")){
            holder.tvName.setVisibility(View.GONE);
        }else{
            holder.tvName.setVisibility(View.VISIBLE);
        }
        if(cardList.get(position).card_address_show.equals("0")){
            holder.llAddress.setVisibility(View.GONE);
        }else{
            holder.llAddress.setVisibility(View.VISIBLE);
        }
        if(cardList.get(position).card_email_show.equals("0")){
            holder.llMail.setVisibility(View.GONE);
        }else{
            holder.llMail.setVisibility(View.VISIBLE);
        }
        if(cardList.get(position).card_phone_show.equals("0")){
            holder.llPhoneNumber.setVisibility(View.GONE);
        }else{
            holder.llPhoneNumber.setVisibility(View.VISIBLE);
        }
        holder.tvOrganisationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardList.get(position).card_org_fontsize_mob));
        holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardList.get(position).card_name_fontsize_mob));
        holder.tvphoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardList.get(position).card_phone_fontsize_mob));
        holder.tvAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardList.get(position).card_address_fontsize_mob));
        holder.tvemailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardList.get(position).card_email_fontsize_mob));
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
