package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ServiceResponseModelClass {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("data")
    @Expose
    public ArrayList<ServiceListDataModel> data;
}
