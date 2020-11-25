package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProfileClass {

    @SerializedName("apicredential")
    @Expose
    public ApiCredentialModel apiCredentialModel;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("user_fname")
    @Expose
    public String user_fname;

    @SerializedName("user_lname")
    @Expose
    public String user_lname;

    @SerializedName("user_address")
    @Expose
    public String user_address;

    @SerializedName("user_pin")
    @Expose
    public String user_pin;

    @SerializedName("user_email")
    @Expose
    public String user_email;

    @SerializedName("user_gender")
    @Expose
    public String user_gender;

    @SerializedName("user_organization_name")
    @Expose
    public String user_organization_name;

    @SerializedName("user_youtube")
    @Expose
    public String user_youtube;

    @SerializedName("user_bio")
    @Expose
    public String user_bio;

}
