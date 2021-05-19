package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceListDataModel {

    @SerializedName("service_id")
    @Expose
    public String service_id;

    @SerializedName("service_name")
    @Expose
    public String service_name;

    @SerializedName("service_slug")
    @Expose
    public String service_slug;

    @SerializedName("service_icon")
    @Expose
    public String service_icon;

    @SerializedName("service_caption")
    @Expose
    public String service_caption;

    @SerializedName("service_image")
    @Expose
    public String service_image;

    @SerializedName("service_created_by")
    @Expose
    public String service_created_by;

    @SerializedName("service_created_at")
    @Expose
    public String service_created_at;

}
