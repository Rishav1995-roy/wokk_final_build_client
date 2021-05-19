package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpModelClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("otp_value")
    @Expose
    public String otp_value;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("user_id")
    @Expose
    public String user_id;
}
