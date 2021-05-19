package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YoutubeDetailsModel {

    @SerializedName("youtube_id")
    @Expose
    public String youtube_id;

    @SerializedName("youtube_user_id")
    @Expose
    public String youtube_user_id;

    @SerializedName("youtube_url")
    @Expose
    public String youtube_url;

    @SerializedName("youtube_embed_url")
    @Expose
    public String youtube_embed_url;

    @SerializedName("youtube_mob_url")
    @Expose
    public String youtube_mob_url;

    @SerializedName("youtube_link_title")
    @Expose
    public String youtube_link_title;

    @SerializedName("youtube_created_by")
    @Expose
    public String youtube_created_by;

    @SerializedName("youtube_created_at")
    @Expose
    public String youtube_created_at;

}
