package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditGalleryClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("gallery_id")
    @Expose
    public String gallery_id;

    @SerializedName("gallery_title")
    @Expose
    public String gallery_title;

    @SerializedName("gallery_caption")
    @Expose
    public String gallery_caption;
}
