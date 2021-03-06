package com.app.wokk.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.CardEditActivity;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.adapter.UserTypeAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.CreateCardClass;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.ServiceClass;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.model.ServiceResponseModelClass;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.wokk.activity.ContainerActivity.getCardResponseDataModel;
import static com.app.wokk.activity.ContainerActivity.viewCount;

public class CreateCardFragment extends BaseFragment implements View.OnClickListener{

    public static CreateCardFragment newInstance() {
        return new CreateCardFragment();
    }
    public File pic;
    public View rootView;
    public MyPreference myPreference;
    String organisationName="";
    public TextInputEditText etFirstName,etLastname,etPhoneNumber,etEmail,etAddress,etPin,etOrganisationame,etDescription;
    public TextView tvuserType,tvEmptyText,tvOrganisationName,tvName,tvCardAddress,tvemailAddress,tvphoneNumber,tvWatermark;
    public RelativeLayout rlUsertype,rlRecyclerView,rlMale,rlFemale,rlOther,rlCard;
    public LinearLayout llAddress,llMail,llPhoneNumber;
    public RecyclerView rvuserType;
    public ImageView ivLoadder,ivOther,ivFemale,ivMale,ivDown;
    public Button btnCreate;
    public boolean genderSelected=false;
    public static boolean userTypeSelected=false;
    public static String serviceID;
    public ArrayList<ServiceListDataModel> servicesList;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView=inflater.inflate(R.layout.fragment_create_card,container,false);
        myPreference=new MyPreference(getActivity());
        //ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
        //ContainerActivity.rlMyCardViews.setVisibility(View.GONE);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        servicesList=new ArrayList<>();
        tvWatermark=rootView.findViewById(R.id.tvWatermark);
        int alpha = 100;
        tvWatermark.setTextColor(Color.argb(alpha, 0, 0, 0));
        llAddress=rootView.findViewById(R.id.llAddress);
        llMail=rootView.findViewById(R.id.llMail);
        llPhoneNumber=rootView.findViewById(R.id.llPhoneNumber);
        rlCard=rootView.findViewById(R.id.rlCard);
        tvCardAddress=rootView.findViewById(R.id.tvCardAddress);
        tvemailAddress=rootView.findViewById(R.id.tvemailAddress);
        tvphoneNumber=rootView.findViewById(R.id.tvphoneNumber);
        tvOrganisationName=rootView.findViewById(R.id.tvOrganisationName);
        tvName=rootView.findViewById(R.id.tvName);
        btnCreate=rootView.findViewById(R.id.btnCreate);
        etDescription=rootView.findViewById(R.id.etDescription);
        ivDown=rootView.findViewById(R.id.ivDown);
        ivMale=rootView.findViewById(R.id.ivMale);
        ivFemale=rootView.findViewById(R.id.ivFemale);
        ivOther=rootView.findViewById(R.id.ivOther);
        ivLoadder=rootView.findViewById(R.id.ivLoadder);
        rvuserType=rootView.findViewById(R.id.rvuserType);
        rlOther=rootView.findViewById(R.id.rlOther);
        rlFemale=rootView.findViewById(R.id.rlFemale);
        rlMale=rootView.findViewById(R.id.rlMale);
        rlRecyclerView=rootView.findViewById(R.id.rlRecyclerView);
        rlUsertype=rootView.findViewById(R.id.rlUsertype);
        tvEmptyText=rootView.findViewById(R.id.tvEmptyText);
        tvuserType=rootView.findViewById(R.id.tvuserType);
        etOrganisationame=rootView.findViewById(R.id.etOrganisationame);
        etEmail=rootView.findViewById(R.id.etEmail);
        etPhoneNumber=rootView.findViewById(R.id.etPhoneNumber);
        etLastname=rootView.findViewById(R.id.etLastname);
        etFirstName=rootView.findViewById(R.id.etFirstName);
        etAddress=rootView.findViewById(R.id.etAddress);
        etPin=rootView.findViewById(R.id.etPin);
        if(getCardResponseDataModel.user_fname != null)
            etFirstName.setText(getCardResponseDataModel.user_fname);
        if(getCardResponseDataModel.user_lname != null)
            etLastname.setText(getCardResponseDataModel.user_lname);
        if(getCardResponseDataModel.user_phone != null)
            etPhoneNumber.setText(getCardResponseDataModel.user_phone);
        if(getCardResponseDataModel.user_email != null)
            etEmail.setText(getCardResponseDataModel.user_email);
        if(getCardResponseDataModel.user_address != null)
            etAddress.setText(getCardResponseDataModel.user_address);
        if(getCardResponseDataModel.user_pin != null)
            etPin.setText(getCardResponseDataModel.user_pin);
        if(getCardResponseDataModel.user_organization_name != null)
            etOrganisationame.setText(getCardResponseDataModel.user_organization_name);
        if(ContainerActivity.cardDetailsResponseModel != null) {
            ContainerActivity.rlViews.setVisibility(View.VISIBLE);
            ContainerActivity.rlCreate.setVisibility(View.GONE);
        }else{
            ContainerActivity.rlViews.setVisibility(View.GONE);
            ContainerActivity.rlCreate.setVisibility(View.VISIBLE);
        }
        tvphoneNumber.setText(etPhoneNumber.getText().toString());
        RelativeLayout.LayoutParams phoneParams = (RelativeLayout.LayoutParams) llPhoneNumber.getLayoutParams();
        phoneParams.topMargin=550;
        phoneParams.leftMargin=700;
        llPhoneNumber.setLayoutParams(phoneParams);
        clickEvent();
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            } else {
                requestPermission();
            }
        }
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
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code ==1){
                        servicesList.clear();
                        servicesList=response.body().data;
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
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private void clickEvent(){
        rlUsertype.setOnClickListener(this);
        rlMale.setOnClickListener(this);
        rlFemale.setOnClickListener(this);
        rlOther.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    tvName.setText(etFirstName.getText().toString()+" "+etLastname.getText().toString());
                    RelativeLayout.LayoutParams tvNameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
                    tvNameParams.topMargin=200;
                    tvNameParams.leftMargin=50;
                    tvName.setLayoutParams(tvNameParams);
                }
            }
        });
        etOrganisationame.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    StringBuilder sb = new StringBuilder(etOrganisationame.getText().toString());
                    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                    organisationName=sb.toString();
                    tvOrganisationName.setText(organisationName);
                    RelativeLayout.LayoutParams tvOrganisationParams = (RelativeLayout.LayoutParams) tvOrganisationName.getLayoutParams();
                    tvOrganisationParams.topMargin=100;
                    tvOrganisationParams.leftMargin=50;
                    tvOrganisationName.setLayoutParams(tvOrganisationParams);
                }
            }
        });
        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    tvCardAddress.setText(etAddress.getText().toString()+"-"+etPin.getText().toString());
                    RelativeLayout.LayoutParams addressParams = (RelativeLayout.LayoutParams) llAddress.getLayoutParams();
                    addressParams.topMargin=550;
                    addressParams.leftMargin=50;
                    llAddress.setLayoutParams(addressParams);
                }
            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    tvemailAddress.setText(etEmail.getText().toString());
                    RelativeLayout.LayoutParams mailParams = (RelativeLayout.LayoutParams) llMail.getLayoutParams();
                    mailParams.leftMargin=700;
                    mailParams.topMargin=450;
                    llMail.setLayoutParams(mailParams);
                }
            }
        });
        if(!etEmail.getText().toString().isEmpty()){
            tvemailAddress.setText(etEmail.getText().toString());
            RelativeLayout.LayoutParams mailParams = (RelativeLayout.LayoutParams) llMail.getLayoutParams();
            mailParams.leftMargin=700;
            mailParams.topMargin=450;
            llMail.setLayoutParams(mailParams);
        }
    }

    private String getFilename() {
        File file = new File("card");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");
        return uriSting;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                if(validation()){
                    boolean networkCheck = NetworkCheck.getInstant(getActivity()).isConnectingToInternet();
                    if (networkCheck) {
                        Bitmap bitmap=Bitmap.createBitmap(rlCard.getWidth(),rlCard.getHeight(),Bitmap.Config.ARGB_8888);
                        Canvas canvas=new Canvas(bitmap);
                        Drawable drawable=rlCard.getBackground();
                        if(drawable != null){
                            drawable.draw(canvas);
                        }else{
                            canvas.drawColor(Color.WHITE);
                        }
                        rlCard.draw(canvas);
                        //rlCard.setDrawingCacheEnabled(true);
                        //Bitmap bitmap = rlCard.getDrawingCache();
                        //String file = getFilename();
                        try {
                            pic = File.createTempFile("card", ".png", Environment.getExternalStorageDirectory());
                            FileOutputStream ostream = new FileOutputStream(pic);
                            if(bitmap == null){
                                Log.d("tag","true");
                            }
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                            ostream.close();
                            rlCard.invalidate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            rlCard.setDrawingCacheEnabled(false);
                        }
                        doCreate(pic);
                    }else{
                        customAlert(getResources().getString(R.string.noInternetText));
                    }
                }
                break;
            case R.id.rlUsertype:
                hideKeyBoardRelativeLayout(rlUsertype);
                if(ivDown.getTag().toString().toLowerCase().equals("down")){
                    ivDown.setTag("up");
                    ivDown.setImageResource(R.drawable.up);
                    rlRecyclerView.setVisibility(View.VISIBLE);
                    setAdapter(servicesList);
                }else if(ivDown.getTag().toString().toLowerCase().equals("up")){
                    ivDown.setTag("down");
                    ivDown.setImageResource(R.drawable.down);
                    rlRecyclerView.setVisibility(View.GONE);
                }
                break;
            case R.id.rlMale:
                hideKeyBoardRelativeLayout(rlMale);
                if(ivMale.getTag().toString().toLowerCase().equals("unselected")){
                    ivMale.setTag("selected");
                    ivOther.setTag("unselected");
                    ivFemale.setTag("unselected");
                    ivMale.setImageResource(R.drawable.check);
                    ivFemale.setImageResource(R.drawable.uncheck);
                    ivOther.setImageResource(R.drawable.uncheck);
                    genderSelected=true;
                }else if(ivMale.getTag().toString().toLowerCase().equals("selected")){
                    ivMale.setTag("unselected");
                    ivMale.setImageResource(R.drawable.uncheck);
                    genderSelected=false;
                }
                break;
            case R.id.rlFemale:
                hideKeyBoardRelativeLayout(rlFemale);
                if(ivFemale.getTag().toString().toLowerCase().equals("unselected")){
                    ivFemale.setTag("selected");
                    ivMale.setTag("unselected");
                    ivOther.setTag("unselected");
                    genderSelected=true;
                    ivFemale.setImageResource(R.drawable.check);
                    ivMale.setImageResource(R.drawable.uncheck);
                    ivOther.setImageResource(R.drawable.uncheck);
                }else if(ivFemale.getTag().toString().toLowerCase().equals("selected")){
                    ivFemale.setTag("unselected");
                    genderSelected=false;
                    ivFemale.setImageResource(R.drawable.uncheck);
                }
                break;
            case R.id.rlOther:
                hideKeyBoardRelativeLayout(rlOther);
                if(ivOther.getTag().toString().toLowerCase().equals("unselected")){
                    ivOther.setTag("selected");
                    ivMale.setTag("unselected");
                    ivFemale.setTag("unselected");
                    ivOther.setImageResource(R.drawable.check);
                    ivMale.setImageResource(R.drawable.uncheck);
                    ivFemale.setImageResource(R.drawable.uncheck);
                    genderSelected=true;
                }else if(ivOther.getTag().toString().toLowerCase().equals("selected")){
                    ivOther.setTag("unselected");
                    ivOther.setImageResource(R.drawable.uncheck);
                    genderSelected=false;
                }
                break;
        }
    }

    private void doCreate(File pic) {
        showRotateDialog();
        RequestBody apiUser=RequestBody.create(MediaType.parse("multipart/form-data"), Constant.apiuser);
        RequestBody apiPass=RequestBody.create(MediaType.parse("multipart/form-data"), Constant.apipass);
        RequestBody userId=RequestBody.create(MediaType.parse("multipart/form-data"), myPreference.getUserID());
        RequestBody firstName=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etFirstName.getText()).toString());
        RequestBody lastname=RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody address=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etAddress.getText()).toString());
        RequestBody email=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etEmail.getText()).toString());
        RequestBody gender = RequestBody.create(MediaType.parse("multipart/form-data"), "0");;
        /*if(ivOther.getTag().toString().toLowerCase().equals("selected")){
            gender=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        }else if(ivMale.getTag().toString().toLowerCase().equals("selected")){
            gender=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        }else if(ivFemale.getTag().toString().toLowerCase().equals("selected")){
            gender=RequestBody.create(MediaType.parse("multipart/form-data"), "2");
        }*/
        RequestBody serviceId=RequestBody.create(MediaType.parse("multipart/form-data"), serviceID);
        RequestBody organisationname=RequestBody.create(MediaType.parse("multipart/form-data"), organisationName);
        RequestBody organisationDescription=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etDescription.getText()).toString());
        RequestBody pin=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etPin.getText()).toString());
        MultipartBody.Part image = null;
        if (pic != null) {
            RequestBody propertyImage = RequestBody.create(MediaType.parse("multipart/form-data"), pic);
            image = MultipartBody.Part.createFormData("card_image", "card.png", propertyImage);
        }
        Call<ResponseBody> do_create_card= RestManager.getInstance().getService().create_card(apiUser,apiPass,userId,firstName,lastname,address,pin,email,gender,serviceId,organisationname,organisationDescription,image);
        do_create_card.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile();
                    }else if(jsonObject.optInt("code") == 9){
                        hideRotateDialog();
                        customAlert("Authentication error occurred.");
                    }else{
                        hideRotateDialog();
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    hideRotateDialog();
                    customAlert("Oops, something went wrong!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
        /*ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        CreateCardClass createCardClass=new CreateCardClass();
        createCardClass.apiCredentialModel=apiCredentialModel;
        createCardClass.user_id=myPreference.getUserID();
        createCardClass.fname= Objects.requireNonNull(etFirstName.getText()).toString();
        createCardClass.lname= Objects.requireNonNull(etLastname.getText()).toString();
        createCardClass.address= Objects.requireNonNull(etAddress.getText()).toString();
        createCardClass.pin= Objects.requireNonNull(etPin.getText()).toString();
        createCardClass.email= Objects.requireNonNull(etEmail.getText()).toString();
        if(ivOther.getTag().toString().toLowerCase().equals("selected")){
            createCardClass.gender="0";
        }else if(ivMale.getTag().toString().toLowerCase().equals("selected")){
            createCardClass.gender="1";
        }else if(ivFemale.getTag().toString().toLowerCase().equals("selected")){
            createCardClass.gender="2";
        }
        createCardClass.service=serviceID;
        createCardClass.org_name= Objects.requireNonNull(etOrganisationame.getText()).toString();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(createCardClass);
        Call<ResponseBody> do_create_card= RestManager.getInstance().getService().create_card(jsonElement);
        do_create_card.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile();
                    }else if(jsonObject.optInt("code") == 9){
                        hideRotateDialog();
                        customAlert("Authentication error occurred.");
                    }else{
                        hideRotateDialog();
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    hideRotateDialog();
                    customAlert("Oops, something went wrong!");
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });*/
    }

    private void getProfile() {
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
                        viewCount=response.body().view_count;
                        ContainerActivity.galleryList=response.body().gallery_details;
                        ContainerActivity.cardDetailsResponseModel=response.body().card_details;
                        ContainerActivity.youtubeDetailsModelArrayList=response.body().youtube_details;
                        ContainerActivity.layoutList=response.body().all_layouts;
                        getCardResponseDataModel=response.body().user_details;
                        viewCount=response.body().view_count;
                        if(viewCount != null){
                            if(Integer.parseInt(viewCount)>1){
                                ContainerActivity.tvViews.setText("Views");
                            }else{
                                ContainerActivity.tvViews.setText("View");
                            }
                            ContainerActivity.tvViewCount.setText(viewCount);
                        }else{
                            ContainerActivity.tvViews.setText("View");
                            ContainerActivity.tvViewCount.setText("0");
                        }
                        ContainerActivity.follow_status=response.body().follow_status;
                        ContainerActivity.follow_count=response.body().no_of_followers;
                        ContainerActivity.validity_status=response.body().validity_status;
                        ContainerActivity.validityDate=response.body().user_details.user_card_valid_until;
                        if(getCardResponseDataModel != null){
                            if(getCardResponseDataModel.user_fname != null){
                                if(getCardResponseDataModel.user_lname != null){
                                    ContainerActivity.tvUser.setText("Hi, "+getCardResponseDataModel.user_fname+" "+getCardResponseDataModel.user_lname);
                                }else{
                                    ContainerActivity.tvUser.setText("Hi, "+getCardResponseDataModel.user_fname);
                                }
                            }else{
                                ContainerActivity.tvUser.setText((getCardResponseDataModel.user_phone));
                            }
                            if(ContainerActivity.cardDetailsResponseModel == null){
                                ContainerActivity.llEditCard.setVisibility(View.GONE);
                                ContainerActivity.llCreateCard.setVisibility(View.VISIBLE);
                                ContainerActivity.llMyCard.setVisibility(View.GONE);
                            }else{
                                ContainerActivity.llEditCard.setVisibility(View.VISIBLE);
                                ContainerActivity.llCreateCard.setVisibility(View.GONE);
                                ContainerActivity.llMyCard.setVisibility(View.VISIBLE);
                            }
                        }
                        customAlertForCreate("Your card has been created successfully.");
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

    private void setAdapter(ArrayList<ServiceListDataModel> userList) {
        if(userList.size() >0){
            rvuserType.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            rvuserType.setLayoutManager(new LinearLayoutManager(getActivity()));
            UserTypeAdapter userTypeAdapter=new UserTypeAdapter(this,userList,tvuserType,ivDown,rlRecyclerView);
            rvuserType.setAdapter(userTypeAdapter);
        }else{
            rvuserType.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    private boolean validation() {
        if(Objects.requireNonNull(etFirstName.getText()).toString().isEmpty()){
            customAlert("Please enter your first name!");
            etFirstName.requestFocus();
            return false;
        }
        /*if(Objects.requireNonNull(etLastname.getText()).toString().isEmpty()){
            customAlert("Please enter your last name!");
            etLastname.requestFocus();
            return false;
        }*/
        if(Objects.requireNonNull(etOrganisationame.getText()).toString().isEmpty()){
            customAlert("Please enter your oraganistion name!");
            etOrganisationame.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etDescription.getText()).toString().isEmpty()){
            customAlert("Please enter your oraganistion description!");
            etDescription.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etAddress.getText()).toString().isEmpty()){
            customAlert("Please enter your address!");
            etAddress.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etPin.getText()).toString().isEmpty()){
            customAlert("Please enter your pincode!");
            etFirstName.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etEmail.getText()).toString().isEmpty()){
            customAlert("Please enter your email address!");
            etEmail.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etPhoneNumber.getText()).toString().isEmpty()){
            customAlert("Please enter your phone number!");
            etPhoneNumber.requestFocus();
            return false;
        }
        if(!userTypeSelected){
            customAlert("Please a select a user type");
            return false;
        }
        /*if(!genderSelected){
            customAlert("Please select any one from the gender.");
            return false;
        }*/
        return true;
    }

    private void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
        customAlertWithOneButton.show();
        customAlertWithOneButton.tvDesc.setText(s);
        customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertWithOneButton.dismiss();
            }
        });
    }

    private void customAlertForCreate(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
        customAlertWithOneButton.show();
        customAlertWithOneButton.tvDesc.setText(s);
        customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAlertWithOneButton.dismiss();
                Fragment homeFragment = HomeFragment.newInstance();
                FragmentTransaction homeTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                homeTransaction.replace(R.id.frameContainerChild, homeFragment);
                homeTransaction.addToBackStack(null);
                homeTransaction.commit();
            }
        });
    }
}
