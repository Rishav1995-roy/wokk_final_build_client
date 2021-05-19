package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCardClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("fname")
    @Expose
    public String fname;

    @SerializedName("lname")
    @Expose
    public String lname;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("pin")
    @Expose
    public String pin;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("gender")
    @Expose
    public String gender;

    @SerializedName("service")
    @Expose
    public String service;

    @SerializedName("org_name")
    @Expose
    public String org_name;

}
