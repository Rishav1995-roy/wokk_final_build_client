package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneNumberResponseModel {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("otp")
    @Expose
    public String otp;

}
