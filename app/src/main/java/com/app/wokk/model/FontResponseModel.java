package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FontResponseModel {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("fonts")
    @Expose
    public ArrayList<String> fonts;

}
