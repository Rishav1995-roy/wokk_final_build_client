package com.app.wokk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CardLiistForParticularServiceResponseModel {

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("all_cards")
    @Expose
    public ArrayList<AllCardsResponseModel> all_cards;
}
