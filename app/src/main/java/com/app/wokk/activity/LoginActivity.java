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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.wokk.R;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.FirebaseTokenRegisterModel;
import com.app.wokk.model.LoginModelClass;
import com.app.wokk.model.LoginResponseModel;
import com.app.wokk.mvp.LoginModel;
import com.app.wokk.mvp.LoginPresenter;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseClass implements View.OnClickListener, LoginModel.View {

    public TextInputEditText etPhoneNumber,etPassword;
    public TextView tvForgot;
    public LinearLayout llSignIn,llLogin;
    public Button btnSignIn;
    public MyPreference myPreference;
    public boolean backPressed;
    LoginModel.Presenter presenter;
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);
        myPreference=new MyPreference(this);
        presenter=new LoginPresenter(this);
        initView();
    }

    private void initView() {
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        etPassword=findViewById(R.id.etPassword);
        tvForgot=findViewById(R.id.tvForgot);
        llSignIn=findViewById(R.id.llSignIn);
        llLogin=findViewById(R.id.llLogin);
        btnSignIn=findViewById(R.id.btnSignIn);
        clickEvent();
    }

    private void clickEvent() {
        llSignIn.setOnClickListener(this);
        llLogin.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tvForgot:
                hideKeyBoardText(tvForgot);
                customAlert("This section is under development.");
                break;
            case R.id.llSignIn:
                hideKeyBoardLinearlayout(llSignIn);
                myPreference.setForgotPasswordLoadStatus(false);
                myPreference.setOtpLoad(false);
                Intent signInIntent=new Intent(this,RegistrationActivity.class);
                startActivity(signInIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.btnSignIn:
                hideKeyBoardButton(btnSignIn);
                if(validation()){
                    boolean networkCheck = NetworkCheck.getInstant(getApplicationContext()).isConnectingToInternet();
                    if (networkCheck) {
                        showRotateDialog();
                        presenter.doLogin(Objects.requireNonNull(etPhoneNumber.getText()).toString(), Objects.requireNonNull(etPassword.getText()).toString());
                    }else{
                        doValidation(getResources().getString(R.string.noInternetText));
                    }
                }
                break;
            case R.id.llLogin:
                hideKeyBoardLinearlayout(llLogin);
                break;
        }
    }

    private boolean validation() {
        if(Objects.requireNonNull(etPhoneNumber.getText()).toString().isEmpty()){
            doValidation("Enter your registered phone number!");
            etPhoneNumber.requestFocus();
            return false;
        }
        if(etPhoneNumber.getText().toString().length() != 10) {
            doValidation("Please enter a valid phone number!");
            etPhoneNumber.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(etPassword.getText()).toString().isEmpty()){
            doValidation("Enter your password!");
            etPassword.requestFocus();
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
                    btnSignIn.setEnabled(true);
                    btnSignIn.setClickable(true);
                    btnSignIn.setFocusable(true);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(!backPressed){
            Toast.makeText(this,"Pree again to exit",Toast.LENGTH_SHORT).show();
            backPressed=true;
        }else{
            finish();
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotateDialog != null && rotateDialog.isShowing()) {
            rotateDialog.dismiss();
        }
        rotateDialog = null;
    }

    @Override
    public void doSuccess(Response<LoginResponseModel> response) {
        assert response.body() != null;
        myPreference.setLoginStatus(true);
        myPreference.setUserID(response.body().data.user_id);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        token = task.getResult();
                        registerDeviceToken(token,myPreference.getUserID());
                    }

                });
    }

    private void registerDeviceToken(String token, String userID) {
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser=Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        FirebaseTokenRegisterModel firebaseTokenRegisterModel=new FirebaseTokenRegisterModel();
        firebaseTokenRegisterModel.apiCredentialModel=apiCredentialModel;
        firebaseTokenRegisterModel.user_id=userID;
        firebaseTokenRegisterModel.device_id=token;
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(firebaseTokenRegisterModel);
        Call<ResponseBody> doRegister=RestManager.getInstance().getService().doFirebaseTokenRegister(jsonElement);
        doRegister.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideRotateDialog();
                try {
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        Intent loginIntent=new Intent(getApplicationContext(),ContainerActivity.class);
                        startActivity(loginIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else if(jsonObject.optInt("code") == 9){
                        doValidation("Authentication error occurred.");
                    }else{
                        doValidation("Oops, something went wrong!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    doValidation("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideRotateDialog();
                doValidation("Oops, something went wrong!");
            }
        });
    }

    @Override
    public void doFailure(int code) {
        hideRotateDialog();
        if(code == 9){
            doValidation("Authentication error occured!");
        }else{
            doValidation("Invaild credentials!");
        }
    }

    @Override
    public void doValidation(String messsage) {
        customAlert(messsage);
    }
}
