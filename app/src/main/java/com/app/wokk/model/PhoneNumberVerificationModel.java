package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneNumberVerificationModel {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("phone")
    @Expose
    public String phone;

}
