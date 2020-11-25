package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("follower_id")
    @Expose
    public String follower_id;

}
