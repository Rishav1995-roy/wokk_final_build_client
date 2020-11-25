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
import com.app.wokk.preference.MyPreference;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhonenumberActivity extends BaseClass implements View.OnClickListener {

    public ImageView ivBack;
    public TextView tvHeading,tvSubHeading;
    public TextInputEditText etPhoneNumber;
    public Button btnDone;
    public ImageView ivLoadder;
    public LinearLayout llPhoneNumber;
    public MyPreference myPreference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_phone_number);
        myPreference=new MyPreference(this);
        initView();
    }

    private void initView() {
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        ivLoadder=findViewById(R.id.ivLoadder);
        ivBack=findViewById(R.id.ivBack);
        tvHeading=findViewById(R.id.tvHeading);
        tvSubHeading=findViewById(R.id.tvSubHeading);
        btnDone=findViewById(R.id.btnDone);
        llPhoneNumber=findViewById(R.id.llPhoneNumber);
        if(myPreference.getOtpLoad()){
            if(!myPreference.getPhoneNumber().equals("")){
                etPhoneNumber.setText(myPreference.getPhoneNumber());
            }
        }
        if(myPreference.getForgotPasswordLoad()){
            tvHeading.setText(R.string.phoneNumberHeading);
            tvSubHeading.setText(R.string.phoneNumberSubHeadingForForgot);
        }else{
            tvHeading.setText(R.string.phoneNumberHeading);
            tvSubHeading.setText(R.string.phonenumbersubheadingForSignUp);
        }
        clickEvent();
    }

    public void clickEvent(){
        llPhoneNumber.setOnClickListener(this);
        btnDone.setOnClickListener(this);
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
            case R.id.btnDone:
                hideKeyBoardButton(btnDone);
                if(validation()){
                    //new asyncTask().execute();
                }
                break;
        }
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
