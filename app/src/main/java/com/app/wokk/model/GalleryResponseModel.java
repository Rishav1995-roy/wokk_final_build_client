package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryResponseModel {

    @SerializedName("gallery_id")
    @Expose
    public String gallery_id;

    @SerializedName("gallery_user_id")
    @Expose
    public String gallery_user_id;

    @SerializedName("gallery_title")
    @Expose
    public String gallery_title;

    @SerializedName("gallery_caption")
    @Expose
    public String gallery_caption;

    @SerializedName("gallery_image")
    @Expose
    public String gallery_image;

    @SerializedName("gallery_image_url")
    @Expose
    public String gallery_image_url;

    @SerializedName("gallery_created_by")
    @Expose
    public String gallery_created_by;

    @SerializedName("gallery_created_at")
    @Expose
    public String gallery_created_at;

}
