package com.app.wokk.activity;

import android.annotation.SuppressLint;
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
import com.app.wokk.model.PhoneNumberResponseModel;
import com.app.wokk.model.PhoneNumberVerificationModel;
import com.app.wokk.model.ResetPasswordModel;
import com.app.wokk.model.ResetPasswordResponseModel;
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

public class ForgotPasswordActivity extends BaseClass implements View.OnClickListener{

    public ImageView ivBack,ivForgot;
    public TextInputEditText etNewPassword,etConfirmPassword;
    public LinearLayout llForgot;
    public MyPreference myPreference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_forgot);
        myPreference=new MyPreference(this);
        initView();
    }

    private void initView() {
        ivBack=findViewById(R.id.ivBack);
        etNewPassword=findViewById(R.id.etNewPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        llForgot=findViewById(R.id.llForgot);
        ivForgot=findViewById(R.id.ivForgot);
        clickEvent();
    }

    private void clickEvent() {
        llForgot.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llForgot:
                hideKeyBoardLinearlayout(llForgot);
                break;
            case R.id.ivBack:
                hideKeyBoardImageView(ivBack);
                myPreference.setOtpLoad(true);
                Intent phoneNumberIntent = new Intent(this, PhonenumberActivity.class);
                startActivity(phoneNumberIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.ivForgot:
                if(validation()){
                    doForgot();
                }
                break;
        }
    }

    private void doForgot() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        ResetPasswordModel resetPasswordModel=new ResetPasswordModel();
        resetPasswordModel.apiCredentialModel=apiCredentialModel;
        resetPasswordModel.user_id=myPreference.getUserIDForForgot();
        resetPasswordModel.password= Objects.requireNonNull(etNewPassword.getText()).toString();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(resetPasswordModel);
        Call<ResetPasswordResponseModel> doReset= RestManager.getInstance().getService().doResetPassword(jsonElement);
        doReset.enqueue(new Callback<ResetPasswordResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<ResetPasswordResponseModel> call, @NotNull Response<ResetPasswordResponseModel> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                       customAlert(response.body().message,true);
                    }else if(code == 9){
                        customAlert("An authentication error occured!",false);
                    }else{
                        customAlert("Oops, something went wrong!",false);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    customAlert("Oops, something went wrong!",false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResetPasswordResponseModel> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.",false);
            }
        });
    }

    private boolean validation() {
        if(Objects.requireNonNull(etNewPassword.getText()).toString().isEmpty()){
           customAlert("Please enter your new password!",false);
           etNewPassword.requestFocus();
           return false;
        }
        if(etNewPassword.getText().toString().length() < 6 ){
            customAlert("Password must have 6 chrarcters",false);
            etNewPassword.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etConfirmPassword.getText()).toString().isEmpty()){
           customAlert("Please confirm your new password!",false);
           etConfirmPassword.requestFocus();
           return false;
        }
        if(!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString())){
           customAlert("Entered passwords are not matching. Please type the same password in both fields.",false);
           return false;
        }
        return true;
    }

    private void customAlert(String s, final boolean bool) {
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
                    if(bool){
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myPreference.setOtpLoad(true);
        Intent phoneNumberIntent = new Intent(this, PhonenumberActivity.class);
        startActivity(phoneNumberIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
