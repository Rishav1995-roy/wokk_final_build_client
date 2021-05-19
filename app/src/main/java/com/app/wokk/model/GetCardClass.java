package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCardClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("logged_in_user_id")
    @Expose
    public String logged_in_user_id;
}
