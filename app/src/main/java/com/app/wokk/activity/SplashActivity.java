package com.app.wokk.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.app.wokk.R;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.preference.MyPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends BaseClass {

    public Handler mHandler;
    public MyPreference myPreference;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash);
        myPreference=new MyPreference(this);
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myPreference.getLoginStatus()) {
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                        return;
                                    }
                                    token = task.getResult();
                                }

                            });
                    myPreference.setLoad(false);
                    myPreference.setFromCardEdit(false);
                    Intent intent = new Intent(getApplicationContext(), ContainerActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(myPreference.getForgotPasswordLoad()){
                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(myPreference.getRegistrationStatus()){
                    Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else{
                    myPreference.setRegistrationStatus(false);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        },2000);
    }
}