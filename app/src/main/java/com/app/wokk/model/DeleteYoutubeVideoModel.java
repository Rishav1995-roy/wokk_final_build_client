package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteYoutubeVideoModel {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("youtube_id")
    @Expose
    public String youtube_id;

}
