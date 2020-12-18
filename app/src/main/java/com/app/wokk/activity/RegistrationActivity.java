package com.app.wokk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.wokk.R;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.RegisterModelClass;
import com.app.wokk.model.RegisterResponseModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseClass implements View.OnClickListener{
    
    public TextView tvLogin,tvTerms;
    public ImageView ivLoadder,ivBack,ivCheck,ivSignUp;
    public TextInputEditText etEmail,etPassword,etPhoneNumber;
    public RelativeLayout rlSignIn;
    public MyPreference myPreference;
    public boolean termsSelected=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_registration);
        myPreference=new MyPreference(this);
        initView();
    }

    private void initView() {
        tvLogin=findViewById(R.id.tvLogin);
        tvTerms=findViewById(R.id.tvTerms);
        ivSignUp=findViewById(R.id.ivSignUp);
        ivBack=findViewById(R.id.ivBack);
        ivCheck=findViewById(R.id.ivCheck);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        rlSignIn=findViewById(R.id.rlSignIn);
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        if(myPreference.getRegistrationStatus()){
            if(!myPreference.getEmail().equals("")){
                etEmail.setText(myPreference.getEmail());
            }
            etPassword.setText(myPreference.getPassword());
            etPhoneNumber.setText(myPreference.getPhoneNumber());
        }
        clickEvent();
    }

    private void clickEvent() {
        ivBack.setOnClickListener(this);
        rlSignIn.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        ivSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rlSignIn:
                hideKeyBoardRelativeLayout(rlSignIn);
                break;
            case R.id.ivBack:
                //myPreference.setOtpLoad(true);
                Intent phoneNumberIntent = new Intent(this, LoginActivity.class);
                startActivity(phoneNumberIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.tvLogin:
                myPreference.setOtpLoad(false);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.tvTerms:
                hideKeyBoardText(tvTerms);
                customAlert("This section is in under development");
                break;
            case R.id.ivCheck:
                hideKeyBoardImageView(ivCheck);
                if (ivCheck.getTag().toString().toLowerCase().equals("uncheck")) {
                    ivCheck.setImageResource(R.drawable.check);
                    ivCheck.setTag("check");
                    termsSelected=true;
                }else if (ivCheck.getTag().toString().toLowerCase().equals("check")) {
                    ivCheck.setImageResource(R.drawable.uncheck);
                    ivCheck.setTag("uncheck");
                    termsSelected=false;
                }
                break;
            case R.id.ivSignUp:
                hideKeyBoardImageView(ivSignUp);
                if(validation()){
                    boolean networkCheck = NetworkCheck.getInstant(getApplicationContext()).isConnectingToInternet();
                    if (networkCheck) {
                        doRegistration();
                    }else{
                        customAlert(getResources().getString(R.string.noInternetText));
                    }
                }
                break;

        }

    }

    private void doRegistration() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        RegisterModelClass registerModelClass=new RegisterModelClass();
        registerModelClass.apiCredentialModel=apiCredentialModel;
        registerModelClass.email= Objects.requireNonNull(etEmail.getText()).toString();
        registerModelClass.password= Objects.requireNonNull(etPassword.getText()).toString();
        registerModelClass.phone= Objects.requireNonNull(etPhoneNumber.getText()).toString();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(registerModelClass);
        Call<RegisterResponseModel> doRegister= RestManager.getInstance().getService().doRegister(jsonElement);
        doRegister.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<RegisterResponseModel> call, @NotNull Response<RegisterResponseModel> response) {
                hideRotateDialog();
                try {
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                        myPreference.setOtpLoad(true);
                        myPreference.setUserID(response.body().user_id);
                        myPreference.setRegistrationStatus(true);
                        myPreference.setPhoneNumber(Objects.requireNonNull(etPhoneNumber.getText()).toString());
                        myPreference.setPassword(Objects.requireNonNull(etPassword.getText()).toString());
                        myPreference.setEmail(Objects.requireNonNull(etEmail.getText()).toString());
                        Intent otpIntent=new Intent(getApplicationContext(), OtpActivity.class);
                        startActivity(otpIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else if(code == 9){
                        customAlert("An authentication error occured!");
                    }else{
                        customAlert("Invaild credentials!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RegisterResponseModel> call, @NotNull Throwable t) {
                customAlert("Internal server error. Please try after few minutes.");
                hideRotateDialog();
            }
        });
    }


    private boolean validation() {
        if(Objects.requireNonNull(etPhoneNumber.getText()).toString().isEmpty()){
            customAlert("Please enter your phone number!");
            return false;
        }
        if(etPhoneNumber.getText().toString().length() != 10) {
            customAlert("Please enter a valid phone number!");
            etPhoneNumber.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etPassword.getText()).toString().isEmpty()){
            customAlert("Please enter your password!");
            etPassword.requestFocus();
            return false;
        }
        if(etPassword.getText().toString().length() < 8 ){
            customAlert("Paswword must have 8 characters!");
            etPassword.requestFocus();
            return false;
        }
        if(!termsSelected){
            customAlert("Please agree to our terms and condition!");
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
        //myPreference.setOtpLoad(true);
        Intent phoneNumberIntent = new Intent(this, LoginActivity.class);
        startActivity(phoneNumberIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
