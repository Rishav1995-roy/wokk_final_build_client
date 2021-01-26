package com.app.wokk.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

public class MyPreference {

    SharedPreferences sharedPreferences;

    public void removeUser(){
        sharedPreferences.edit().clear().apply();
    }

    public Boolean getLoginStatus() {
        return  sharedPreferences.getBoolean("loginStatus",false);
    }

    public void setLoginStatus(Boolean load) {
        sharedPreferences.edit().putBoolean("loginStatus",load).apply();
    }

    public Boolean getFromCardEdit() {
        return  sharedPreferences.getBoolean("fromCardEdit",false);
    }

    public void setFromCardEdit(Boolean load) {
        sharedPreferences.edit().putBoolean("fromCardEdit",load).apply();
    }

    public Boolean getRegistrationStatus() {
        return  sharedPreferences.getBoolean("registrationStatus",false);
    }

    public void setRegistrationStatus(Boolean status) {
        sharedPreferences.edit().putBoolean("registrationStatus",status).apply();
    }

    public Boolean getLoad() {
        return  sharedPreferences.getBoolean("load",false);
    }

    public void setLoad(Boolean load) {
        sharedPreferences.edit().putBoolean("load",load).apply();
    }

    public Boolean getForgotPasswordLoad(){
        return  sharedPreferences.getBoolean("forgotPasswordLoad",false);
    }

    public void setForgotPasswordLoadStatus(Boolean load){
        sharedPreferences.edit().putBoolean("forgotPasswordLoad",load).apply();
    }

    public Boolean getOtpLoad(){
        return  sharedPreferences.getBoolean("otpLoad",false);
    }

    public void setOtpLoad(Boolean load){
        sharedPreferences.edit().putBoolean("otpLoad",load).apply();
    }

    public Boolean getForPasswordLoad(){
        return  sharedPreferences.getBoolean("forgotLoad",false);
    }

    public void setForgotPasswordLoad(Boolean load){
        sharedPreferences.edit().putBoolean("forgotLoad",load).apply();
    }

    public String getPhoneNumber(){
        return sharedPreferences.getString("phoneNumber","");
    }

    public void setPhoneNumber(String number){
        sharedPreferences.edit().putString("phoneNumber",number).apply();
    }

    public String getUserID(){
        return sharedPreferences.getString("userID","");
    }

    public void setUserID(String userID){
        sharedPreferences.edit().putString("userID",userID).apply();
    }

    public String getServiceUserId(){
        return sharedPreferences.getString("serviceUserID","");
    }

    public void setServiceUserID(String userID){
        sharedPreferences.edit().putString("serviceUserID",userID).apply();
    }

    public String getServiceId(){
        return sharedPreferences.getString("serviceID","");
    }

    public void setServiceID(String userID){
        sharedPreferences.edit().putString("serviceID",userID).apply();
    }

    public String getLayout(){
        return sharedPreferences.getString("layout","");
    }

    public void setLayout(String layout){
        sharedPreferences.edit().putString("layout",layout).apply();
    }

    public String getLayoutID(){
        return sharedPreferences.getString("layoutID","");
    }

    public void setLayoutID(String layoutID){
        sharedPreferences.edit().putString("layoutID",layoutID).apply();
    }

    public String getPassword(){
        return sharedPreferences.getString("password","");
    }

    public void setPassword(String password){
        sharedPreferences.edit().putString("password",password).apply();
    }

    public String getEmail(){
        return sharedPreferences.getString("email","");
    }

    public void setEmail(String email){
        sharedPreferences.edit().putString("email",email).apply();
    }

    public String getOtpForForgot(){
        return sharedPreferences.getString("otp","");
    }

    public void setOtpForForgot(String otp){
        sharedPreferences.edit().putString("otp",otp).apply();
    }

    public String getUserIDForForgot(){
        return sharedPreferences.getString("userIDForgot","");
    }

    public void setUserIDForgot(String userId){
        sharedPreferences.edit().putString("userIDForgot",userId).apply();
    }

    public String getFireBaseToken(){
        return sharedPreferences.getString("token","");
    }

    public void setFireBaseToken(String token){
        sharedPreferences.edit().putString("token",token).apply();
    }

    public MyPreference(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
