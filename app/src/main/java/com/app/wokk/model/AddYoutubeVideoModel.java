package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddYoutubeVideoModel {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("youtube_link_title")
    @Expose
    public String youtube_link_title;

    @SerializedName("youtube_url")
    @Expose
    public String youtube_url;
}
