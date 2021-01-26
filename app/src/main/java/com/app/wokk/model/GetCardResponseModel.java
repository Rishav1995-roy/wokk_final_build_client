package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetCardResponseModel {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("user_details")
    @Expose
    public GetCardResponseDataModel user_details;

    @SerializedName("card_details")
    @Expose
    public CardDetailsResponseModel card_details;

    @SerializedName("gallery_details")
    @Expose
    public ArrayList<GalleryResponseModel> gallery_details;

    @SerializedName("youtube_details")
    @Expose
    public ArrayList<YoutubeDetailsModel> youtube_details;

    @SerializedName("view_count")
    @Expose
    public String view_count;

    @SerializedName("all_layouts")
    @Expose
    public ArrayList<AllLayoutsResponseModel> all_layouts;

    @SerializedName("layout_url")
    @Expose
    public String layout_url;

    @SerializedName("follow_status")
    @Expose
    public int follow_status;

    @SerializedName("no_of_followers")
    @Expose
    public String no_of_followers;

    @SerializedName("validity_status")
    @Expose
    public boolean  validity_status;


    @SerializedName("default_card_layout")
    @Expose
    public int  default_card_layout;
}
