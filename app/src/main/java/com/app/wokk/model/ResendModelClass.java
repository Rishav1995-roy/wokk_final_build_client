package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendModelClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("user_id")
    @Expose
    public String user_id;
}
