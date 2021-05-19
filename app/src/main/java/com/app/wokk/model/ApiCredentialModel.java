package com.app.wokk.model;

import com.app.wokk.retrofit.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiCredentialModel {

    @SerializedName("apiuser")
    @Expose
    public String apiuser;

    @SerializedName("apipass")
    @Expose
    public String apipass;
}
