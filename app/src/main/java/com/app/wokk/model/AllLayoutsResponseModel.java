package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllLayoutsResponseModel {

    @SerializedName("layout_id")
    @Expose
    public String layout_id;

    @SerializedName("layout_name")
    @Expose
    public String layout_name;

    @SerializedName("layout_service_id")
    @Expose
    public String layout_service_id;

    @SerializedName("layout_image")
    @Expose
    public String layout_image;

    @SerializedName("created_by")
    @Expose
    public String created_by;

    @SerializedName("created_at")
    @Expose
    public String user_email;

    @SerializedName("modified_by")
    @Expose
    public String modified_by;

}
