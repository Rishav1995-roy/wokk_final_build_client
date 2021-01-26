package com.app.wokk.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.wokk.R;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.PhoneNumberResponseModel;
import com.app.wokk.model.PhoneNumberVerificationModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.wokk.fragment.HomeFragment.llNumber;

public class PhonenumberActivity extends BaseClass implements View.OnClickListener {

    public ImageView ivBack,ivSubmit;
    public TextView tvHeading,tvSubHeading;
    public TextInputEditText etPhoneNumber;
    public LinearLayout llPhoneNumber;
    public MyPreference myPreference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_phone_number);
        myPreference=new MyPreference(this);
        initView();
    }

    private void initView() {
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        ivBack=findViewById(R.id.ivBack);
        tvHeading=findViewById(R.id.tvHeading);
        tvSubHeading=findViewById(R.id.tvSubHeading);
        ivSubmit=findViewById(R.id.ivSubmit);
        llPhoneNumber=findViewById(R.id.llPhoneNumber);
        if(myPreference.getOtpLoad()){
            if(!myPreference.getPhoneNumber().equals("")){
                etPhoneNumber.setText(myPreference.getPhoneNumber());
            }
        }
        clickEvent();
    }

    public void clickEvent(){
        llPhoneNumber.setOnClickListener(this);
        ivSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                hideKeyBoardImageView(ivBack);
                myPreference.setOtpLoad(false);
                Intent loginIntent=new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.llPhoneNumber:
                hideKeyBoardLinearlayout(llPhoneNumber);
                break;
            case R.id.ivSubmit:
                hideKeyBoardImageView(ivSubmit);
                if(validation()){
                   doVerifyPhoneNumber();
                }
                break;
        }
    }

    private void doVerifyPhoneNumber() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        PhoneNumberVerificationModel phoneNumberVerificationModel=new PhoneNumberVerificationModel();
        phoneNumberVerificationModel.apiCredentialModel=apiCredentialModel;
        phoneNumberVerificationModel.phone= Objects.requireNonNull(etPhoneNumber.getText()).toString();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(phoneNumberVerificationModel);
        Call<PhoneNumberResponseModel> doVerifyPhoneno= RestManager.getInstance().getService().doVerifyPhoneNumber(jsonElement);
        doVerifyPhoneno.enqueue(new Callback<PhoneNumberResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<PhoneNumberResponseModel> call, @NotNull Response<PhoneNumberResponseModel> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                       myPreference.setOtpForForgot(response.body().otp);
                       myPreference.setUserIDForgot(response.body().user_id);
                        myPreference.setOtpLoad(false);
                        myPreference.setRegistrationStatus(false);
                        myPreference.setForgotPasswordLoad(true);
                        myPreference.setPhoneNumber(Objects.requireNonNull(etPhoneNumber.getText()).toString());
                        Intent otpIntent=new Intent(getApplicationContext(), OtpActivity.class);
                        startActivity(otpIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
            public void onFailure(@NotNull Call<PhoneNumberResponseModel> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private boolean validation() {
        if(Objects.requireNonNull(etPhoneNumber.getText()).toString().isEmpty()){
            customAlert("Please enter your mobile number!");
            etPhoneNumber.requestFocus();
            return false;
        }
        if(etPhoneNumber.getText().toString().length() != 10){
            customAlert("Please enter a valid phone number!");
            etPhoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    private void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(this);
        if(!customAlertWithOneButton.isShowing()){
            customAlertWithOneButton.show();
            customAlertWithOneButton.setCanceledOnTouchOutside(false);
            customAlertWithOneButton.setCancelable(false);
            customAlertWithOneButton.tvDesc.setText(s);
            customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithOneButton.dismiss();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myPreference.setOtpLoad(false);
        Intent loginIntent=new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotateDialog != null && rotateDialog.isShowing()) {
            rotateDialog.dismiss();
        }
        rotateDialog = null;
    }
}
