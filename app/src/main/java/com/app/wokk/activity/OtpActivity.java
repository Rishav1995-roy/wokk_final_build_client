package com.app.wokk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.wokk.R;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.FirebaseTokenRegisterModel;
import com.app.wokk.model.OtpModelClass;
import com.app.wokk.model.OtpResponseModel;
import com.app.wokk.model.ResendModelClass;
import com.app.wokk.model.ResendResponseModel;
import com.app.wokk.mvp.OtpModel;
import com.app.wokk.mvp.OtpPresenter;
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

public class OtpActivity extends BaseClass implements View.OnClickListener, OtpModel.View {

    public ImageView ivLoadder,ivBack;;
    public TextView tvResend,tvFourth,tvThird,tvSecond,tvFirst,tvSubHeading;
    public View viewOne,viewTwo,viewThree,viewFour;
    public LinearLayout llText;
    public RelativeLayout rlOtp;
    public EditText etText;
    public MyPreference myPreference;
    public CountDownTimer countDownTimer;
    OtpModel.Presenter presenter;
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_otp);
        myPreference = new MyPreference(this);
        presenter=new OtpPresenter(this);
        initView();
    }

    private void initView() {
        ivLoadder = findViewById(R.id.ivLoadder);
        tvSubHeading = findViewById(R.id.tvSubHeading);
        ivBack = findViewById(R.id.ivBack);
        tvResend = findViewById(R.id.tvResend);
        tvFourth = findViewById(R.id.tvFourth);
        tvThird = findViewById(R.id.tvThird);
        tvSecond = findViewById(R.id.tvSecond);
        tvFirst = findViewById(R.id.tvFirst);
        viewOne = findViewById(R.id.viewOne);
        viewTwo = findViewById(R.id.viewTwo);
        viewThree = findViewById(R.id.viewThree);
        viewFour = findViewById(R.id.viewFour);
        rlOtp = findViewById(R.id.rlOtp);
        llText = findViewById(R.id.llText);
        etText = findViewById(R.id.etText);
        String lastTwoCharacterOfPhone=myPreference.getPhoneNumber().substring(myPreference.getPhoneNumber().length() - 2);
        tvSubHeading.setText("We have send an otp on ********"+lastTwoCharacterOfPhone+"\n to verify your account.");
        clickevent();
        etText.requestFocus();
        if (etText.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void clickevent() {
        rlOtp.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvResend.setOnClickListener(this);
        llText.setOnClickListener(this);
        startTimer();
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 1){
                    tvFirst.setText(String.valueOf(s.charAt(0)));
                    tvSecond.setText("");
                    tvThird.setText("");
                    tvFourth.setText("");
                    viewOne.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewThree.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    viewFour.setBackgroundColor(getResources().getColor(R.color.offBlack));
                }else if(s.toString().length() == 2){
                    viewOne.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewThree.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewFour.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    tvFirst.setText(String.valueOf(s.charAt(0)));
                    tvSecond.setText(String.valueOf(s.charAt(1)));
                    tvThird.setText("");
                    tvFourth.setText("");
                }else if(s.toString().length() == 3){
                    viewOne.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewThree.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewFour.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvFirst.setText(String.valueOf(s.charAt(0)));
                    tvSecond.setText(String.valueOf(s.charAt(1)));
                    tvThird.setText(String.valueOf(s.charAt(2)));
                    tvFourth.setText("");
                } else if(s.toString().length() == 4){
                    viewOne.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewThree.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewFour.setBackgroundColor(getResources().getColor(R.color.orange));
                    tvFirst.setText(String.valueOf(s.charAt(0)));
                    tvSecond.setText(String.valueOf(s.charAt(1)));
                    tvThird.setText(String.valueOf(s.charAt(2)));
                    tvFourth.setText(String.valueOf(s.charAt(3)));
                    hideKeyBoardText(tvFourth);
                    boolean networkCheck = NetworkCheck.getInstant(getApplicationContext()).isConnectingToInternet();
                    if (networkCheck) {
                        ivLoadder.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(R.raw.loadder).into(ivLoadder);
                        String otp=Objects.requireNonNull(tvFirst.getText()).toString() + Objects.requireNonNull(tvSecond.getText()).toString() + Objects.requireNonNull(tvThird.getText()).toString() + Objects.requireNonNull(tvFourth.getText()).toString();
                        if(myPreference.getForgotPasswordLoad()){
                            if(otp.equals(myPreference.getOtpForForgot())){
                                Intent forgotIntent=new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                                startActivity(forgotIntent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            }else{
                                customAlert("Please enter the correct otp for change your password!");
                            }
                        }else {
                            presenter.doCheckOtp(otp, myPreference.getPhoneNumber(), myPreference.getUserID());
                        }
                    }else{
                        doError(getResources().getString(R.string.noInternetText));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    viewOne.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    viewThree.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    viewFour.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    tvFirst.setText("");
                    tvSecond.setText("");
                    tvThird.setText("");
                    tvFourth.setText("");
                }
            }
        });
    }

    @Override
    public void doSuccess(Response<OtpResponseModel> response) {
        rlOtp.setAlpha(1.0f);
        ivBack.setEnabled(true);
        tvFirst.setEnabled(true);
        tvSecond.setEnabled(true);
        tvThird.setEnabled(true);
        tvFourth.setEnabled(true);
        tvResend.setEnabled(true);
        ivLoadder.setVisibility(View.GONE);
        if (myPreference.getForgotPasswordLoad()) {
            Intent forgotIntent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(forgotIntent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            myPreference.setLoginStatus(true);
            myPreference.setRegistrationStatus(false);
            myPreference.setFromCardEdit(false);
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
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        Intent loginIntent=new Intent(getApplicationContext(),ContainerActivity.class);
                        startActivity(loginIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }else if(jsonObject.optInt("code") == 9){
                        customAlert("Authentication error occurred.");
                    }else{
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideRotateDialog();
                customAlert("Oops, something went wrong!");
            }
        });
    }

    @Override
    public void doFailure(int code) {
        rlOtp.setAlpha(1.0f);
        ivBack.setEnabled(true);
        tvFirst.setEnabled(true);
        tvSecond.setEnabled(true);
        tvThird.setEnabled(true);
        tvFourth.setEnabled(true);
        tvResend.setEnabled(true);
        ivLoadder.setVisibility(View.GONE);
        doError("Otp does not match. Please check your otp!");
    }

    @Override
    public void doError(String message) {
        customAlert(message);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                int seconds = (int) l / 1000;
                tvResend.setEnabled(false);
                tvResend.setClickable(false);
                tvResend.setAlpha(0.4f);
                if (seconds < 10) {
                    tvResend.setText("Resend in " + "00:0" + seconds);
                } else {
                    tvResend.setText("Resend in " + "00:" + seconds);
                }
            }

            @Override
            public void onFinish() {
                tvResend.setText("Resend");
                tvResend.setEnabled(true);
                tvResend.setClickable(true);
                tvResend.setAlpha(1.0f);
            }
        }.start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llText:
                if (etText.requestFocus()) {
                    hideKeyboard(this);
                }
                break;
            case R.id.ivBack:
                hideKeyBoardImageView(ivBack);
                if (ivLoadder.getVisibility() == View.GONE) {
                    if (myPreference.getForgotPasswordLoad()) {
                        myPreference.setOtpLoad(true);
                        Intent phoneNumberIntent = new Intent(this, PhonenumberActivity.class);
                        startActivity(phoneNumberIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else {
                        Intent phoneNumberIntent = new Intent(this, RegistrationActivity.class);
                        startActivity(phoneNumberIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }
                break;
            case R.id.rlOtp:
                hideKeyBoardRelativeLayout(rlOtp);
                break;
            case R.id.tvResend:
                hideKeyBoardText(tvResend);
                boolean networkCheck = NetworkCheck.getInstant(getApplicationContext()).isConnectingToInternet();
                if (networkCheck) {
                    tvFirst.setText("");
                    tvSecond.setText("");
                    tvThird.setText("");
                    tvFourth.setText("");
                    viewOne.setBackgroundColor(getResources().getColor(R.color.orange));
                    viewTwo.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    viewThree.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    viewFour.setBackgroundColor(getResources().getColor(R.color.offBlack));
                    doResend();
                }else{
                    customAlert(getResources().getString(R.string.noInternetText));
                }
                break;
        }

    }

    private void doResend() {
        rlOtp.setAlpha(0.4f);
        ivBack.setEnabled(false);
        tvFirst.setEnabled(false);
        tvSecond.setEnabled(false);
        tvThird.setEnabled(false);
        tvFourth.setEnabled(false);
        tvResend.setEnabled(false);
        ivLoadder.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(R.raw.loadder).into(ivLoadder);
        ApiCredentialModel apiCredentialModel = new ApiCredentialModel();
        apiCredentialModel.apiuser = Constant.apiuser;
        apiCredentialModel.apipass = Constant.apipass;
        ResendModelClass resendModelClass=new ResendModelClass();
        resendModelClass.apiCredentialModel=apiCredentialModel;
        resendModelClass.phone=myPreference.getPhoneNumber();
        if(myPreference.getForgotPasswordLoad()){
            resendModelClass.user_id=myPreference.getUserIDForForgot();
        }else {
            resendModelClass.user_id = myPreference.getUserID();
        }
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(resendModelClass);
        Call<ResendResponseModel> doResend=RestManager.getInstance().getService().resend_otp(jsonElement);
        doResend.enqueue(new Callback<ResendResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<ResendResponseModel> call, @NotNull Response<ResendResponseModel> response) {
                try{
                    assert response.body() != null;
                    int code = response.body().code;
                    rlOtp.setAlpha(1.0f);
                    ivBack.setEnabled(true);
                    tvFirst.setEnabled(true);
                    tvSecond.setEnabled(true);
                    tvThird.setEnabled(true);
                    tvFourth.setEnabled(true);
                    tvResend.setEnabled(true);
                    ivLoadder.setVisibility(View.GONE);
                    if(code == 1){
                       customAlert("An otp has been sent your phone number successfully.");
                    }else{
                        customAlert("Otp does not match. Please check your otp!");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    rlOtp.setAlpha(1.0f);
                    ivBack.setEnabled(true);
                    tvFirst.setEnabled(true);
                    tvSecond.setEnabled(true);
                    tvThird.setEnabled(true);
                    tvFourth.setEnabled(true);
                    tvResend.setEnabled(true);
                    ivLoadder.setVisibility(View.GONE);
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResendResponseModel> call, @NotNull Throwable t) {
                rlOtp.setAlpha(1.0f);
                ivBack.setEnabled(true);
                tvFirst.setEnabled(true);
                tvSecond.setEnabled(true);
                tvThird.setEnabled(true);
                tvFourth.setEnabled(true);
                tvResend.setEnabled(true);
                ivLoadder.setVisibility(View.GONE);
                customAlert("Internal server error. Please try after few minutes.");
            }
        });

    }

    private void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton = new CustomAlertWithOneButton(this);
        if (!customAlertWithOneButton.isShowing()) {
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
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        if (rotateDialog != null && rotateDialog.isShowing()) {
            rotateDialog.dismiss();
        }
        rotateDialog = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ivLoadder.getVisibility() == View.GONE) {
            countDownTimer.cancel();
            if (myPreference.getForgotPasswordLoad()) {
                myPreference.setOtpLoad(true);
                Intent phoneNumberIntent = new Intent(this, PhonenumberActivity.class);
                startActivity(phoneNumberIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                // myPreference.setOtpLoad(true);
                Intent phoneNumberIntent = new Intent(this, RegistrationActivity.class);
                startActivity(phoneNumberIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }
    }

    void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        assert view != null;
        boolean isKeyboardShowing = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (isKeyboardShowing) {
            imm.showSoftInput(activity.getCurrentFocus(), 0);
        } else {
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
