package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponseModel {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("message")
    @Expose
    public String message;

}
