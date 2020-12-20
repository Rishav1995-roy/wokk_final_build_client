package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCardResponseDataModel {

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("user_organization_desc")
    @Expose
    public String user_organization_desc;

    @SerializedName("user_card_valid_until")
    @Expose
    public String user_card_valid_until;

    @SerializedName("user_token")
    @Expose
    public String user_token;

    @SerializedName("user_fname")
    @Expose
    public String user_fname;

    @SerializedName("user_lname")
    @Expose
    public String user_lname;

    @SerializedName("user_organization_name")
    @Expose
    public String user_organization_name;

    @SerializedName("user_email")
    @Expose
    public String user_email;

    @SerializedName("user_password")
    @Expose
    public String user_password;

    @SerializedName("user_phone")
    @Expose
    public String user_phone;

    @SerializedName("user_address")
    @Expose
    public String user_address;

    @SerializedName("user_pin")
    @Expose
    public String user_pin;

    @SerializedName("user_bio")
    @Expose
    public String user_bio;

    @SerializedName("user_youtube")
    @Expose
    public String user_youtube;

    @SerializedName("user_gender")
    @Expose
    public String user_gender;

    @SerializedName("user_role")
    @Expose
    public String user_role;

    @SerializedName("user_status")
    @Expose
    public String user_status;

    @SerializedName("user_service_id")
    @Expose
    public String user_service_id;

    @SerializedName("user_payment_status")
    @Expose
    public String user_payment_status;

    @SerializedName("user_payment_validity")
    @Expose
    public String user_payment_validity;

    @SerializedName("user_created_at")
    @Expose
    public String user_created_at;

}
