package com.app.wokk.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.adapter.CardYoutubeAdapter;
import com.app.wokk.adapter.YoutubeAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAddYoutubeLinkAlert;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.AddYoutubeVideoModel;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.DeleteYoutubeVideoModel;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.YoutubeDetailsModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserYoutubeFragment extends BaseFragment {

    public static UserYoutubeFragment newInstance() {
        return new UserYoutubeFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public RecyclerView rvYoutube;
    public TextView tvEmptyText;
    public LinearLayout llUpload;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView=inflater.inflate(R.layout.fragment_user_youtube,container,false);
        myPreference=new MyPreference(getActivity());
        //ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        llUpload=rootView.findViewById(R.id.llUpload);
        rvYoutube=rootView.findViewById(R.id.rvYoutube);
        tvEmptyText=rootView.findViewById(R.id.tvEmptyText);
        setUpYoutubeAdapter(ContainerActivity.youtubeDetailsModelArrayList);
        llUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoardLinearlayout(llUpload);
                customVideoAddAlert();
            }
        });
    }

    private void customVideoAddAlert() {
        final CustomAddYoutubeLinkAlert customAddYoutubeLinkAlert=new CustomAddYoutubeLinkAlert(Objects.requireNonNull(getActivity()));
        customAddYoutubeLinkAlert.show();
        customAddYoutubeLinkAlert.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAddYoutubeLinkAlert.dismiss();
            }
        });
        customAddYoutubeLinkAlert.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation(customAddYoutubeLinkAlert)){
                    customAddYoutubeLinkAlert.dismiss();
                    boolean networkCheck = NetworkCheck.getInstant(getActivity()).isConnectingToInternet();
                    if (networkCheck) {
                        addVideo(customAddYoutubeLinkAlert.etCaption,customAddYoutubeLinkAlert.etTitle);
                    }else{
                        customAlert(getResources().getString(R.string.noInternetText));
                    }
                }
            }
        });
    }

    private void addVideo(TextInputEditText etCaption, TextInputEditText etTitle) {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        AddYoutubeVideoModel addYoutubeVideoModel=new AddYoutubeVideoModel();
        addYoutubeVideoModel.apiCredentialModel=apiCredentialModel;
        addYoutubeVideoModel.user_id=myPreference.getUserID();
        addYoutubeVideoModel.youtube_link_title= Objects.requireNonNull(etTitle.getText()).toString();
        addYoutubeVideoModel.youtube_url= Objects.requireNonNull(etCaption.getText()).toString();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(addYoutubeVideoModel);
        Call<ResponseBody> doDelete= RestManager.getInstance().getService().addVideo(jsonElement);
        doDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try{
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile(1);
                    }else if(jsonObject.optInt("code") == 9){
                        hideRotateDialog();
                        customAlert("Authentication error occurred.");
                    }else{
                        hideRotateDialog();
                        customAlert("Oops, something went wrong!");
                    }
                }catch(Exception e){
                    hideRotateDialog();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call,@NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private boolean validation(CustomAddYoutubeLinkAlert customAddYoutubeLinkAlert) {
        if(Objects.requireNonNull(customAddYoutubeLinkAlert.etTitle.getText()).toString().isEmpty()){
            customAlert("Please add a title of your video!");
            return false;
        }
        if(Objects.requireNonNull(customAddYoutubeLinkAlert.etCaption.getText()).toString().isEmpty()){
            customAlert("Please add a link of your video!");
            return false;
        }
        Pattern youtubePattern = Pattern.compile("http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)");
        boolean isValid = youtubePattern.matcher(customAddYoutubeLinkAlert.etCaption.getText().toString()).matches();
        if(!isValid){
            customAlert("Please add a proper link of the video!");
            return false;
        }
        return true;
    }

    private void setUpYoutubeAdapter(ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList) {
        if(youtubeDetailsModelArrayList.size() > 0){
            rvYoutube.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            rvYoutube.setLayoutManager(new LinearLayoutManager(getActivity()));
            YoutubeAdapter youtubeAdapter=new YoutubeAdapter(this,youtubeDetailsModelArrayList);
            rvYoutube.setAdapter(youtubeAdapter);
        }else{
            rvYoutube.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    public void deleteVideo(String youtube_id){
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        DeleteYoutubeVideoModel deleteYoutubeVideoModel=new DeleteYoutubeVideoModel();
        deleteYoutubeVideoModel.apiCredentialModel=apiCredentialModel;
        deleteYoutubeVideoModel.youtube_id=youtube_id;
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(deleteYoutubeVideoModel);
        Call<ResponseBody> doDelete= RestManager.getInstance().getService().deleteVideo(jsonElement);
        doDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try{
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile(2);
                    }else if(jsonObject.optInt("code") == 9){
                        hideRotateDialog();
                        customAlert("Authentication error occurred.");
                    }else{
                        hideRotateDialog();
                        customAlert("Oops, something went wrong!");
                    }
                }catch(Exception e){
                    hideRotateDialog();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call,@NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private void getProfile(final int id) {
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        GetCardClass getCardClass=new GetCardClass();
        getCardClass.apiCredentialModel=apiCredentialModel;
        getCardClass.user_id=myPreference.getUserID();
        getCardClass.logged_in_user_id=myPreference.getUserID();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(getCardClass);
        Call<GetCardResponseModel> getprofile= RestManager.getInstance().getService().get_profile(jsonElement);
        getprofile.enqueue(new Callback<GetCardResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<GetCardResponseModel> call, @NotNull Response<GetCardResponseModel> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                        ContainerActivity.galleryList.clear();
                        ContainerActivity.layoutList.clear();
                        ContainerActivity.youtubeDetailsModelArrayList.clear();
                        ContainerActivity.layoutUrl=response.body().layout_url;
                        ContainerActivity.viewCount=response.body().view_count;
                        ContainerActivity.galleryList=response.body().gallery_details;
                        ContainerActivity.cardDetailsResponseModel=response.body().card_details;
                        ContainerActivity.youtubeDetailsModelArrayList=response.body().youtube_details;
                        ContainerActivity.layoutList=response.body().all_layouts;
                        ContainerActivity.getCardResponseDataModel=response.body().user_details;
                        ContainerActivity.follow_status=response.body().follow_status;
                        ContainerActivity.follow_count=response.body().no_of_followers;
                        ContainerActivity.validity_status=response.body().validity_status;
                        ContainerActivity.validityDate=response.body().user_details.user_card_valid_until;
                        if(id == 1){
                            customAlert("Video has been added successfully.");
                            setUpYoutubeAdapter(ContainerActivity.youtubeDetailsModelArrayList);
                        }else if(id == 2){
                            customAlert("Video has been deleted successfully.");
                            setUpYoutubeAdapter(ContainerActivity.youtubeDetailsModelArrayList);
                        }
                    }else if(code == 9){
                        customAlert("An authentication error occured!");
                    }else{
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCardResponseModel> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });

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
}
