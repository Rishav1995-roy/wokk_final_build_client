package com.app.wokk.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.adapter.FontFamilyAdapter;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.customAlert.EditCardDialog;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.EditCardClass;
import com.app.wokk.model.FontFamilyModel;
import com.app.wokk.model.FontResponseModel;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.app.wokk.utility.ColorPicker;
import com.app.wokk.utility.OnDragTouchListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.wokk.activity.ContainerActivity.cardDetailsResponseModel;

public class CardEditActivity extends BaseClass implements View.OnClickListener {

    public MyPreference myPreference;
    public File pic;
    public File card_pic;
    String organisationName="";
    public TextView tvOrganisationName,tvphoneNumber,tvName,tvAddress,tvemailAddress,tvAttributeFont,tvEmptyText;
    public LinearLayout llAddress,llMail,llPhoneNumber,llEdit,llChangeLayout,llSave,llPreview,llSideView,llLowerView,lllogo;
    public RelativeLayout rlCard,rlUsertype,rlRecyclerView,rlColorHolder,rlBorderColorHolder,rlBorder;
    public ImageView ivBack,ivDown, ivCard, ivAddress, ivMail, ivPhone,cardLogo;
    public ToggleButton toggleAttribute;
    public RecyclerView rvFont;
    public EditText etFont;
    public boolean oraganisationVisibility,nameVisibility,addressVisibility,emailVisibility,phoneVisibility;
    public String organisationFontvalue,nameFontValue,addressFontValue,emailFontvalue,phoneFontvalue,organisationColor,nameColor,addressColor,emailColor,phoneColor,borderColor,organisationFont,nameFont,addressFont,emailFont,phoneFont,textSelectd="",layoutId;
    Typeface organistionTypeface,nameTypeface,addressTypeface,emailtypeface,phoneTypeface;
    ArrayList<String> fontFamilyList;
    int heightInProtait,widthInProtait;
    DisplayMetrics metrics;
    int tendp;
    int twentydp;
    String[] permissions = { android.Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_card_edit);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        tendp=getResources().getDimensionPixelOffset(R.dimen._10sdp);
        twentydp=getResources().getDimensionPixelOffset(R.dimen._20sdp);
        float density = metrics.density;
        heightInProtait = metrics.heightPixels;
        widthInProtait =metrics.widthPixels;
        myPreference=new MyPreference(this);
        //requestPermission();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean networkCheck = NetworkCheck.getInstant(this).isConnectingToInternet();
        if (networkCheck) {
            getFontFamily();
        }else{
            customAlert(getResources().getString(R.string.noInternetText));
        }
    }

    private void getFontFamily() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        FontFamilyModel fontFamilyModel=new FontFamilyModel();
        fontFamilyModel.apiCredentialModel=apiCredentialModel;
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(fontFamilyModel);
        Call<FontResponseModel> getFontfamily= RestManager.getInstance().getService().getFontFamily(jsonElement);
        getFontfamily.enqueue(new Callback<FontResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<FontResponseModel> call, @NotNull Response<FontResponseModel> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                        fontFamilyList=new ArrayList<>();
                        fontFamilyList.clear();
                        fontFamilyList.addAll(response.body().fonts);
                    }else if(code == 9){
                        customAlert("An authentication error occured!");
                    }else{
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<FontResponseModel> call,@NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        cardLogo=findViewById(R.id.cardLogo);
        lllogo=findViewById(R.id.lllogo);
        rlBorder=findViewById(R.id.rlBorder);
        etFont=findViewById(R.id.etFont);
        ivPhone=findViewById(R.id.ivPhone);
        ivMail=findViewById(R.id.ivMail);
        ivAddress=findViewById(R.id.ivAddress);
        llLowerView=findViewById(R.id.llLowerView);
        rlBorderColorHolder=findViewById(R.id.rlBorderColorHolder);
        ivCard=findViewById(R.id.ivCard);
        llSideView=findViewById(R.id.llSideView);
        toggleAttribute=findViewById(R.id.toggleAttribute);
        tvOrganisationName=findViewById(R.id.tvOrganisationName);
        tvphoneNumber=findViewById(R.id.tvphoneNumber);
        tvName=findViewById(R.id.tvName);
        tvAddress=findViewById(R.id.tvAddress);
        tvemailAddress=findViewById(R.id.tvemailAddress);
        tvAttributeFont=findViewById(R.id.tvAttributeFont);
        tvEmptyText=findViewById(R.id.tvEmptyText);
        llAddress=findViewById(R.id.llAddress);
        llMail=findViewById(R.id.llMail);
        llPhoneNumber=findViewById(R.id.llPhoneNumber);
        llEdit=findViewById(R.id.llEdit);
        llChangeLayout=findViewById(R.id.llChangeLayout);
        llSave=findViewById(R.id.llSave);
        rlCard=findViewById(R.id.rlCard);
        rlUsertype=findViewById(R.id.rlUsertype);
        rlRecyclerView=findViewById(R.id.rlRecyclerView);
        rlColorHolder=findViewById(R.id.rlColorHolder);
        ivBack=findViewById(R.id.ivBack);
        ivDown=findViewById(R.id.ivDown);
        rvFont=findViewById(R.id.rvFont);
        llPreview=findViewById(R.id.llPreview);
        etFont.setClickable(true);
        etFont.setInputType(InputType.TYPE_NULL);
        //RelativeLayout  mainLayout= (RelativeLayout ) findViewById(R.id.mainLayout);
        setData();
        clickEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        if(ContainerActivity.getCardResponseDataModel.user_fname != null && ContainerActivity.getCardResponseDataModel.user_lname != null)
            tvName.setText(ContainerActivity.getCardResponseDataModel.user_fname+" "+ContainerActivity.getCardResponseDataModel.user_lname);
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(10, Color.parseColor(cardDetailsResponseModel.card_border_color));
        rlCard.setBackgroundDrawable(gd);
        tvOrganisationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_org_fontsize_mob));
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_name_fontsize_mob));
        tvphoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_phone_fontsize_mob));
        tvAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_address_fontsize_mob));
        tvemailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_email_fontsize_mob));
        if(myPreference.getLayout().equals("")){
            Glide.with(this).load(ContainerActivity.layoutUrl + cardDetailsResponseModel.layout_image).into(ivCard);
        }else{
            Glide.with(this).load(myPreference.getLayout()).into(ivCard);
        }


        if(ContainerActivity.getCardResponseDataModel.user_address != null && ContainerActivity.getCardResponseDataModel.user_pin != null)
            tvAddress.setText(ContainerActivity.getCardResponseDataModel.user_address+" - "+ ContainerActivity.getCardResponseDataModel.user_pin);
        if(ContainerActivity.getCardResponseDataModel.user_phone != null)
            tvphoneNumber.setText(ContainerActivity.getCardResponseDataModel.user_phone);
        if(ContainerActivity.getCardResponseDataModel.user_organization_name != null)
            tvOrganisationName.setText(ContainerActivity.getCardResponseDataModel.user_organization_name);
        if(ContainerActivity.getCardResponseDataModel.user_email != null)
            tvemailAddress.setText(ContainerActivity.getCardResponseDataModel.user_email);
        RelativeLayout.LayoutParams tvNameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
        if(cardDetailsResponseModel.card_name_top_mob != null) {
            //int topmargin=(Integer.parseInt(String.valueOf(ContainerActivity.cardDetailsResponseModel.card_name_top_mob));
            tvNameParams.topMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_name_top_mob));
        }else
            tvNameParams.topMargin=0;
        if(cardDetailsResponseModel.card_name_left_mob != null) {
            int leftmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_name_left_mob)));
            tvNameParams.leftMargin = leftmargin;
        }else
            tvNameParams.leftMargin=0;
        tvName.setLayoutParams(tvNameParams);
        RelativeLayout.LayoutParams tvOrganisationParams = (RelativeLayout.LayoutParams) tvOrganisationName.getLayoutParams();
        if(cardDetailsResponseModel.card_org_top_mob != null) {
            int topmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_org_top_mob)));
            tvOrganisationParams.topMargin = topmargin;
        }else
            tvOrganisationParams.topMargin=0;
        if(cardDetailsResponseModel.card_org_left_mob != null) {
            int leftmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_org_left_mob)));
            tvOrganisationParams.leftMargin = leftmargin;
        }else
            tvOrganisationParams.leftMargin=0;
        tvOrganisationName.setLayoutParams(tvOrganisationParams);
        RelativeLayout.LayoutParams addressParams = (RelativeLayout.LayoutParams) llAddress.getLayoutParams();
        if(cardDetailsResponseModel.card_address_top_mob != null) {
            int topmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_address_top_mob)));
            addressParams.topMargin = topmargin;
        }else
            addressParams.topMargin=0;
        if(cardDetailsResponseModel.card_address_left_mob != null) {
            int leftmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_address_left_mob)));
            addressParams.leftMargin = leftmargin;
        }else
            addressParams.leftMargin=0;
        llAddress.setLayoutParams(addressParams);
        RelativeLayout.LayoutParams phoneParams = (RelativeLayout.LayoutParams) llPhoneNumber.getLayoutParams();
        if(cardDetailsResponseModel.card_phone_top_mob != null) {
            int topmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_phone_top_mob)));
            phoneParams.topMargin = topmargin;
        }else
            phoneParams.topMargin=0;
        if(cardDetailsResponseModel.card_phone_left_mob != null) {
            int leftmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_phone_left_mob)));
            phoneParams.leftMargin = leftmargin;
        }else
            phoneParams.leftMargin=0;
        llPhoneNumber.setLayoutParams(phoneParams);
        RelativeLayout.LayoutParams mailParams = (RelativeLayout.LayoutParams) llMail.getLayoutParams();
        if(cardDetailsResponseModel.card_email_top_mob != null) {
            int topmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_email_top_mob)));
            mailParams.topMargin = topmargin;
        }else
            mailParams.topMargin=0;
        if(cardDetailsResponseModel.card_email_left_mob != null) {
            int leftmargin=(Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_email_left_mob)));
            mailParams.leftMargin = leftmargin;
        }else
            mailParams.leftMargin=0;
        llMail.setLayoutParams(mailParams);
        if(cardDetailsResponseModel.card_org_color.contains("0") && cardDetailsResponseModel.card_org_color.length() == 4){
            tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color+"000"));
            organisationColor= cardDetailsResponseModel.card_org_color+"000";
        }else if(cardDetailsResponseModel.card_org_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_org_color.length() == 4){
            tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color.toLowerCase()+"fff"));
            organisationColor= cardDetailsResponseModel.card_org_color.toLowerCase()+"fff";
        }else{
            tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color));
            organisationColor= cardDetailsResponseModel.card_org_color;
        }
        if(cardDetailsResponseModel.card_name_color.contains("0") && cardDetailsResponseModel.card_name_color.length() == 4){
            tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color+"000"));
            nameColor= cardDetailsResponseModel.card_name_color+"000";
        }else if(cardDetailsResponseModel.card_name_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_name_color.length() == 4){
            tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color.toLowerCase()+"fff"));
            nameColor= cardDetailsResponseModel.card_name_color.toLowerCase()+"fff";
        }else{
            tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color));
            nameColor= cardDetailsResponseModel.card_name_color;
        }
        if(cardDetailsResponseModel.card_address_color.contains("0") && cardDetailsResponseModel.card_address_color.length() == 4){
            tvAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color+"000"));
            addressColor= cardDetailsResponseModel.card_address_color+"000";
        }else if(cardDetailsResponseModel.card_address_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_address_color.length() == 4){
            tvAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color.toLowerCase()+"fff"));
            addressColor= cardDetailsResponseModel.card_address_color.toLowerCase()+"fff";
        }else{
            tvAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color));
            addressColor= cardDetailsResponseModel.card_address_color;
        }
        ImageViewCompat.setImageTintMode(ivAddress, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(ivAddress, ColorStateList.valueOf(Color.parseColor(addressColor)));
        if(cardDetailsResponseModel.card_email_color.contains("0") && cardDetailsResponseModel.card_email_color.length() == 4){
            tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color+"000"));
            emailColor= cardDetailsResponseModel.card_email_color+"000";
        }else if(cardDetailsResponseModel.card_email_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_email_color.length() == 4){
            tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color.toLowerCase()+"fff"));
            emailColor= cardDetailsResponseModel.card_email_color.toLowerCase()+"fff";
        }else{
            tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color));
            emailColor= cardDetailsResponseModel.card_email_color;
        }
        ImageViewCompat.setImageTintMode(ivMail, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(ivMail, ColorStateList.valueOf(Color.parseColor(emailColor)));
        if(cardDetailsResponseModel.card_phone_color.contains("0") && cardDetailsResponseModel.card_phone_color.length() == 4){
            tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color+"000"));
            phoneColor= cardDetailsResponseModel.card_phone_color+"000";
        }else if(cardDetailsResponseModel.card_phone_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_phone_color.length() == 4){
            tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color.toLowerCase()+"fff"));
            phoneColor= cardDetailsResponseModel.card_phone_color.toLowerCase()+"fff";
        }else{
            tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color));
            phoneColor= cardDetailsResponseModel.card_phone_color;
        }
        ImageViewCompat.setImageTintMode(ivPhone, PorterDuff.Mode.SRC_ATOP);
        ImageViewCompat.setImageTintList(ivPhone, ColorStateList.valueOf(Color.parseColor(phoneColor)));
        if(cardDetailsResponseModel.card_org_show.equals("0")){
            oraganisationVisibility=false;
            //tvOrganisationName.setVisibility(View.GONE);
        }else{
            oraganisationVisibility=true;
            //tvOrganisationName.setVisibility(View.VISIBLE);
        }
        if(cardDetailsResponseModel.card_name_show.equals("0")){
            //tvName.setVisibility(View.GONE);
            nameVisibility=false;
        }else{
            //tvName.setVisibility(View.VISIBLE);
            nameVisibility=true;
        }
        if(cardDetailsResponseModel.card_address_show.equals("0")){
            //llAddress.setVisibility(View.GONE);
            addressVisibility=false;
        }else{
            //llAddress.setVisibility(View.VISIBLE);
            addressVisibility=true;
        }
        if(cardDetailsResponseModel.card_email_show.equals("0")){
            //llMail.setVisibility(View.GONE);
            emailVisibility=false;
        }else{
            //llMail.setVisibility(View.VISIBLE);
            emailVisibility=true;
        }
        if(cardDetailsResponseModel.card_phone_show.equals("0")){
            //llPhoneNumber.setVisibility(View.GONE);
            phoneVisibility=false;
        }else{
            //llPhoneNumber.setVisibility(View.VISIBLE);
            phoneVisibility=true;
        }
        if(cardDetailsResponseModel.card_org_font != null){
            if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("courier new, monospace")){
                organistionTypeface=getResources().getFont(R.font.courierprime_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("inconsolata")){
                phoneTypeface=getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("recursive")){
                organistionTypeface=getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("cedarville cursive")){
                organistionTypeface=getResources().getFont(R.font.courierprime_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("noto sans")){
                organistionTypeface=getResources().getFont(R.font.notosans_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("poppins")){
                organistionTypeface=getResources().getFont(R.font.poppins_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("open sans")){
                organistionTypeface=getResources().getFont(R.font.opensans_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("roboto")){
                organistionTypeface=getResources().getFont(R.font.roboto_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("montserrat")){
                organistionTypeface=getResources().getFont(R.font.montserrat_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("lato")){
                organistionTypeface=getResources().getFont(R.font.lato_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("source sans pro")){
                organistionTypeface=getResources().getFont(R.font.sourcesanspro_regular);
                tvOrganisationName.setTypeface(organistionTypeface);
            }else if(cardDetailsResponseModel.card_org_font.toLowerCase().equals("raleway, sans-serif")){
                organistionTypeface=getResources().getFont(R.font.raleway_variablefont_wght);
                tvOrganisationName.setTypeface(organistionTypeface);
            }
            organisationFont= cardDetailsResponseModel.card_org_font;
        }
        if(cardDetailsResponseModel.card_name_font != null){
            if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("courier new, monospace")){
                nameTypeface=getResources().getFont(R.font.courierprime_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("inconsolata")){
                nameTypeface=getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("recursive")){
                nameTypeface=getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("cedarville cursive")){
                nameTypeface=getResources().getFont(R.font.courierprime_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("noto sans")){
                nameTypeface=getResources().getFont(R.font.notosans_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("poppins")){
                nameTypeface=getResources().getFont(R.font.poppins_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("open sans")){
                nameTypeface=getResources().getFont(R.font.opensans_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("roboto")){
                nameTypeface=getResources().getFont(R.font.roboto_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("montserrat")){
                nameTypeface=getResources().getFont(R.font.montserrat_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("lato")){
                nameTypeface=getResources().getFont(R.font.lato_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("source sans pro")){
                nameTypeface=getResources().getFont(R.font.sourcesanspro_regular);
                tvName.setTypeface(nameTypeface);
            }else if(cardDetailsResponseModel.card_name_font.toLowerCase().equals("raleway, sans-serif")){
                nameTypeface=getResources().getFont(R.font.raleway_variablefont_wght);
                tvName.setTypeface(nameTypeface);
            }
            nameFont= cardDetailsResponseModel.card_name_font;
        }
        if(cardDetailsResponseModel.card_email_font != null){
            if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("courier new, monospace")){
                emailtypeface=getResources().getFont(R.font.courierprime_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("inconsolata")){
                emailtypeface=getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("recursive")){
                emailtypeface=getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("cedarville cursive")){
                emailtypeface=getResources().getFont(R.font.courierprime_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("noto sans")){
                emailtypeface=getResources().getFont(R.font.notosans_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("poppins")){
                emailtypeface=getResources().getFont(R.font.poppins_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("open sans")){
                emailtypeface=getResources().getFont(R.font.opensans_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("roboto")){
                emailtypeface=getResources().getFont(R.font.roboto_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("montserrat")){
                emailtypeface=getResources().getFont(R.font.montserrat_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("lato")){
                emailtypeface=getResources().getFont(R.font.lato_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("source sans pro")){
                emailtypeface=getResources().getFont(R.font.sourcesanspro_regular);
                tvemailAddress.setTypeface(emailtypeface);
            }else if(cardDetailsResponseModel.card_email_font.toLowerCase().equals("raleway, sans-serif")){
                emailtypeface=getResources().getFont(R.font.raleway_variablefont_wght);
                tvemailAddress.setTypeface(emailtypeface);
            }
            emailFont= cardDetailsResponseModel.card_email_font;
        }
        if(cardDetailsResponseModel.card_address_font != null){
            if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("courier new, monospace")){
                addressTypeface=getResources().getFont(R.font.courierprime_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("inconsolata")){
                addressTypeface=getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("recursive")){
                addressTypeface=getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("cedarville cursive")){
                addressTypeface=getResources().getFont(R.font.courierprime_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("noto sans")){
                addressTypeface=getResources().getFont(R.font.notosans_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("poppins")){
                addressTypeface=getResources().getFont(R.font.poppins_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("open sans")){
                addressTypeface=getResources().getFont(R.font.opensans_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("roboto")){
                addressTypeface=getResources().getFont(R.font.roboto_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("montserrat")){
                addressTypeface=getResources().getFont(R.font.montserrat_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("lato")){
                addressTypeface=getResources().getFont(R.font.lato_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("source sans pro")){
                addressTypeface=getResources().getFont(R.font.sourcesanspro_regular);
                tvAddress.setTypeface(addressTypeface);
            }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("raleway, sans-serif")){
                addressTypeface=getResources().getFont(R.font.raleway_variablefont_wght);
                tvAddress.setTypeface(addressTypeface);
            }
            addressFont= cardDetailsResponseModel.card_address_font;
        }
        if(cardDetailsResponseModel.card_phone_font != null){
            if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("courier new, monospace")){
                phoneTypeface=getResources().getFont(R.font.courierprime_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("inconsolata")){
                phoneTypeface=getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("recursive")){
                phoneTypeface=getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("cedarville cursive")){
                phoneTypeface=getResources().getFont(R.font.courierprime_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("noto sans")){
                phoneTypeface=getResources().getFont(R.font.notosans_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("poppins")){
                phoneTypeface=getResources().getFont(R.font.poppins_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("open sans")){
                phoneTypeface=getResources().getFont(R.font.opensans_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("roboto")){
                phoneTypeface=getResources().getFont(R.font.roboto_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("montserrat")){
                phoneTypeface=getResources().getFont(R.font.montserrat_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("lato")){
                phoneTypeface=getResources().getFont(R.font.lato_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("source sans pro")){
                phoneTypeface=getResources().getFont(R.font.sourcesanspro_regular);
                tvphoneNumber.setTypeface(phoneTypeface);
            }else if(cardDetailsResponseModel.card_phone_font.toLowerCase().equals("raleway, sans-serif")){
                phoneTypeface=getResources().getFont(R.font.raleway_variablefont_wght);
                tvphoneNumber.setTypeface(phoneTypeface);
            }
            phoneFont= cardDetailsResponseModel.card_phone_font;
        }
        if(myPreference.getLayout().equals("")){
            layoutId= cardDetailsResponseModel.layout_id;
        }else{
            layoutId=myPreference.getLayoutID();
        }
        if(cardDetailsResponseModel.card_border_color.contains("0") && cardDetailsResponseModel.card_border_color.length() == 4){
            //tvOrganisationName.setTextColor(Color.parseColor(ContainerActivity.cardDetailsResponseModel.card_org_color+"000"));
            borderColor= cardDetailsResponseModel.card_border_color+"000";
        }else if(cardDetailsResponseModel.card_border_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_border_color.length() == 4){
            //tvOrganisationName.setTextColor(Color.parseColor(ContainerActivity.cardDetailsResponseModel.card_org_color.toLowerCase()+"fff"));
            borderColor= cardDetailsResponseModel.card_border_color.toLowerCase()+"fff";
        }else{
            //tvOrganisationName.setTextColor(Color.parseColor(ContainerActivity.cardDetailsResponseModel.card_org_color));
            borderColor= cardDetailsResponseModel.card_border_color;
        }
        rlBorderColorHolder.setBackgroundColor(Color.parseColor(borderColor));

    }
    private void clickEvent(){
        etFont.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()) {
                    switch (textSelectd) {
                        case "organisation":
                            organisationFontvalue = editable.toString();
                            tvOrganisationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(organisationFontvalue));
                            break;
                        case "name":
                            nameFontValue = editable.toString();
                            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(nameFontValue));
                            break;
                        case "address":
                            addressFontValue = editable.toString();
                            tvAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(addressFontValue));
                            break;
                        case "mail":
                            emailFontvalue = editable.toString();
                            tvemailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(emailFontvalue));
                            break;
                        case "phone":
                            phoneFontvalue = editable.toString();
                            tvphoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(phoneFontvalue));
                            break;
                    }
                }
            }
        });
        llEdit.setOnClickListener(this);
        lllogo.setOnClickListener(this);
        etFont.setOnClickListener(this);
        llPreview.setOnClickListener(this);
        llSave.setOnClickListener(this);
        llChangeLayout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rlBorder.setOnClickListener(this);
        rlCard.setOnClickListener(this);
        cardLogo.setOnTouchListener(new View.OnTouchListener() {
            float dX;
            float dY;
            int lastAction;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        textSelectd="";
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        //lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(DashBoardActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                            break;

                    default:
                        return false;
                }
                return true;
            }
        });
        tvOrganisationName.setOnTouchListener(new View.OnTouchListener() {

            float dX;
            float dY;
            int lastAction;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        textSelectd="organisation";
                        tvOrganisationName.setBackground(getResources().getDrawable(R.drawable.black_bg));
                        tvName.setBackground(null);
                        llPhoneNumber.setBackground(null);
                        llAddress.setBackground(null);
                        llMail.setBackground(null);
                        etFont.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etFont.setClickable(false);
                        etFont.setFocusableInTouchMode(true);
                        if(organisationFontvalue != null)
                            etFont.setText(organisationFontvalue);
                        else
                            etFont.setText(cardDetailsResponseModel.card_org_fontsize_mob);
                        toggleAttribute.setChecked(oraganisationVisibility);
                        rlColorHolder.setBackgroundColor(Color.parseColor(organisationColor));
                        if(cardDetailsResponseModel.card_org_font!= null)
                            tvAttributeFont.setText(cardDetailsResponseModel.card_org_font);
                        else
                            tvAttributeFont.setHint("Select font");
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        //lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        //lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(DashBoardActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return false;
                }
                return true;
            }
        });
        tvName.setOnTouchListener(new View.OnTouchListener() {
                float dX;
                float dY;
                int lastAction;
                @Override
                public boolean onTouch (View view, MotionEvent event){
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            textSelectd="name";
                            tvName.setBackground(getResources().getDrawable(R.drawable.black_bg));
                            tvOrganisationName.setBackground(null);
                            llPhoneNumber.setBackground(null);
                            llAddress.setBackground(null);
                            llMail.setBackground(null);
                            toggleAttribute.setChecked(nameVisibility);
                            etFont.setInputType(InputType.TYPE_CLASS_NUMBER);
                            etFont.setClickable(false);
                            etFont.setFocusableInTouchMode(true);
                            if(nameFontValue != null)
                                etFont.setText(nameFontValue);
                            else
                                etFont.setText(cardDetailsResponseModel.card_name_fontsize_mob);
                            rlColorHolder.setBackgroundColor(Color.parseColor(nameColor));
                            if(cardDetailsResponseModel.card_name_font!= null)
                                tvAttributeFont.setText(cardDetailsResponseModel.card_name_font);
                            else
                                tvAttributeFont.setHint("Select font");
                            dX = view.getX() - event.getRawX();
                            dY = view.getY() - event.getRawY();
                            //lastAction = MotionEvent.ACTION_DOWN;
                            break;

                        case MotionEvent.ACTION_MOVE:
                            view.animate()
                                    .x(event.getRawX() + dX)
                                    .y(event.getRawY() + dY)
                                    .setDuration(0)
                                    .start();
                            //lastAction = MotionEvent.ACTION_MOVE;
                            break;

                        case MotionEvent.ACTION_UP:
                            if (lastAction == MotionEvent.ACTION_DOWN)
                                //Toast.makeText(DashBoardActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                                break;

                        default:
                            return false;
                    }
                    return true;
                }
        });
        llAddress.setOnTouchListener(new View.OnTouchListener() {
            float dX;
            float dY;
            int lastAction;
            @Override
            public boolean onTouch (View view, MotionEvent event){
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        textSelectd="address";
                        llAddress.setBackground(getResources().getDrawable(R.drawable.black_bg));
                        tvName.setBackground(null);
                        llPhoneNumber.setBackground(null);
                        tvOrganisationName.setBackground(null);
                        llMail.setBackground(null);
                        toggleAttribute.setChecked(addressVisibility);
                        etFont.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etFont.setClickable(false);
                        etFont.setFocusableInTouchMode(true);
                        if(addressFontValue != null)
                            etFont.setText(addressFontValue);
                        else
                            etFont.setText(cardDetailsResponseModel.card_address_fontsize_mob);
                        rlColorHolder.setBackgroundColor(Color.parseColor(addressColor));
                        if(cardDetailsResponseModel.card_address_font!= null)
                            tvAttributeFont.setText(cardDetailsResponseModel.card_address_font);
                        else
                            tvAttributeFont.setHint("Select font");
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        //lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                       // lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(DashBoardActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                            break;

                    default:
                        return false;
                }
                return true;
            }
        });
        llMail.setOnTouchListener(new View.OnTouchListener() {
            float dX;
            float dY;
            int lastAction;
            @Override
            public boolean onTouch (View view, MotionEvent event){
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        textSelectd="mail";
                        llMail.setBackground(getResources().getDrawable(R.drawable.black_bg));
                        tvName.setBackground(null);
                        llPhoneNumber.setBackground(null);
                        llAddress.setBackground(null);
                        tvOrganisationName.setBackground(null);
                        toggleAttribute.setChecked(emailVisibility);
                        etFont.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etFont.setClickable(false);
                        etFont.setFocusableInTouchMode(true);
                        if(emailFontvalue != null)
                            etFont.setText(emailFontvalue);
                        else
                            etFont.setText(cardDetailsResponseModel.card_email_fontsize_mob);
                        rlColorHolder.setBackgroundColor(Color.parseColor(emailColor));
                        if(cardDetailsResponseModel.card_email_font!= null)
                            tvAttributeFont.setText(cardDetailsResponseModel.card_email_font);
                        else
                            tvAttributeFont.setHint("Select font");
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        //lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        //lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(DashBoardActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                            break;

                    default:
                        return false;
                }
                return true;
            }
        });
        llPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            float dX;
            float dY;
            int lastAction;
            @Override
            public boolean onTouch (View view, MotionEvent event){
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        textSelectd="phone";
                        llPhoneNumber.setBackground(getResources().getDrawable(R.drawable.black_bg));
                        tvName.setBackground(null);
                        tvOrganisationName.setBackground(null);
                        llAddress.setBackground(null);
                        llMail.setBackground(null);
                        toggleAttribute.setChecked(phoneVisibility);
                        etFont.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etFont.setClickable(false);
                        etFont.setFocusableInTouchMode(true);
                        if(phoneFontvalue != null)
                            etFont.setText(phoneFontvalue);
                        else
                            etFont.setText(cardDetailsResponseModel.card_phone_fontsize_mob);
                        rlColorHolder.setBackgroundColor(Color.parseColor(phoneColor));
                        if(cardDetailsResponseModel.card_phone_font!= null)
                            tvAttributeFont.setText(cardDetailsResponseModel.card_phone_font);
                        else
                            tvAttributeFont.setHint("Select font");
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                       // lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        //lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            //Toast.makeText(DashBoardActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                            break;

                    default:
                        return false;
                }
                return true;
            }
        });
        rlUsertype.setOnClickListener(this);
        rlColorHolder.setOnClickListener(this);
        toggleAttribute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    switch (textSelectd) {
                        case "organisation":
                            oraganisationVisibility = true;
                            //tvOrganisationName.setVisibility(View.VISIBLE);
                            break;
                        case "name":
                            nameVisibility = true;
                            //tvName.setVisibility(View.VISIBLE);
                            break;
                        case "address":
                            addressVisibility = true;
                            //llAddress.setVisibility(View.VISIBLE);
                            break;
                        case "mail":
                            emailVisibility = true;
                            //llMail.setVisibility(View.VISIBLE);
                            break;
                        case "phone":
                            phoneVisibility = true;
                            //llPhoneNumber.setVisibility(View.VISIBLE);
                            break;
                        default:
                            Toast.makeText(CardEditActivity.this,"Please select a attribute",Toast.LENGTH_SHORT).show();
                            toggleAttribute.setChecked(false);
                            break;
                    }
                }else{
                    switch (textSelectd) {
                        case "organisation":
                            oraganisationVisibility = false;
                            //tvOrganisationName.setVisibility(View.GONE);
                            break;
                        case "name":
                            nameVisibility = false;
                            //tvName.setVisibility(View.GONE);
                            break;
                        case "address":
                            addressVisibility = false;
                            //llAddress.setVisibility(View.GONE);
                            break;
                        case "mail":
                            emailVisibility = false;
                            //llMail.setVisibility(View.GONE);
                            break;
                        case "phone":
                            phoneVisibility = false;
                            //llPhoneNumber.setVisibility(View.GONE);
                            break;
                    }
                }
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 201);
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 201:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    assert selectedImage != null;
                    Cursor c = Objects.requireNonNull(CardEditActivity.this).getContentResolver().query(selectedImage, filePath, null, null, null);
                    assert c != null;
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    BitmapFactory.Options Options = new BitmapFactory.Options();
                    Options.inSampleSize = 4;
                    Options.inJustDecodeBounds = false;
                    Bitmap bitmap = (BitmapFactory.decodeFile(picturePath,Options));
                    Uri uri = getImageUri(Objects.requireNonNull(CardEditActivity.this), bitmap);
                    pic = new File(getRealPathFromURI(uri));
                    cardLogo.setImageBitmap(bitmap);
                }
                break;
        }
    }

    Uri getImageUri(Context inContext, Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
        return Uri.parse(path);
    }

    String getRealPathFromURI(Uri uri){
        String path = "";
        if (Objects.requireNonNull(CardEditActivity.this).getContentResolver() != null) {
            Cursor cursor = Objects.requireNonNull(CardEditActivity.this).getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lllogo:
                hideKeyBoardLinearlayout(lllogo);
                requestPermission();
                break;
            case R.id.rlCard:
                textSelectd="";
                etFont.setClickable(true);
                etFont.setInputType(InputType.TYPE_NULL);
                etFont.setText("");
                tvAttributeFont.setText("");
                tvAttributeFont.setHint("Select font");
                toggleAttribute.setChecked(false);
                rlColorHolder.setBackgroundColor(0);
                llPhoneNumber.setBackground(null);
                tvName.setBackground(null);
                tvOrganisationName.setBackground(null);
                llAddress.setBackground(null);
                llMail.setBackground(null);
                break;
            case R.id.etFont:
                if(textSelectd.equals(""))
                    Toast.makeText(CardEditActivity.this,"Please select a attribute first",Toast.LENGTH_SHORT).show();
                break;
            case R.id.llPreview:
                llSideView.setVisibility(View.GONE);
                llLowerView.setVisibility(View.GONE);
                ivBack.setTag("preview");
                if(oraganisationVisibility){
                    tvOrganisationName.setVisibility(View.VISIBLE);
                }else{
                    tvOrganisationName.setVisibility(View.GONE);
                }
                if(nameVisibility){
                    tvName.setVisibility(View.VISIBLE);
                }else{
                    tvName.setVisibility(View.GONE);
                }
                if(addressVisibility){
                    llAddress.setVisibility(View.VISIBLE);
                }else{
                    llAddress.setVisibility(View.GONE);
                }
                if(phoneVisibility){
                    llPhoneNumber.setVisibility(View.VISIBLE);
                }else{
                    llPhoneNumber.setVisibility(View.GONE);
                }
                if(emailVisibility){
                    llMail.setVisibility(View.VISIBLE);
                }else{
                    llMail.setVisibility(View.GONE);
                }
                break;
            case R.id.llEdit:
                if(textSelectd.equals("")){
                    Toast.makeText(CardEditActivity.this,"Please select a attribute first",Toast.LENGTH_SHORT).show();
                }else{
                    switch (textSelectd) {
                        case "organisation":
                            editDialog(tvOrganisationName,textSelectd);
                            break;
                        case "name":
                            editDialog(tvName, textSelectd);
                            break;
                        case "address":
                            editDialog(tvAddress, textSelectd);
                            break;
                        case "mail":
                            editDialog(tvemailAddress, textSelectd);
                            break;
                        case "phone":
                            Toast.makeText(CardEditActivity.this,"You caan't edit the phone number.",Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                break;
            case R.id.llSave:
                hideKeyBoardLinearlayout(llSave);
                llPhoneNumber.setBackground(null);
                tvName.setBackground(null);
                tvOrganisationName.setBackground(null);
                llAddress.setBackground(null);
                llMail.setBackground(null);
                Bitmap bitmap=Bitmap.createBitmap(rlCard.getWidth(),rlCard.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas=new Canvas(bitmap);
                Drawable drawable=rlCard.getBackground();
                if(drawable != null){
                    drawable.draw(canvas);
                }else{
                    canvas.drawColor(Color.WHITE);
                }
                rlCard.draw(canvas);
                        /*rlCard.setDrawingCacheEnabled(true);
                        Bitmap bitmap = rlCard.getDrawingCache();*/
                try {
                    card_pic = File.createTempFile("card", ".png", Environment.getExternalStorageDirectory());
                    FileOutputStream ostream = new FileOutputStream(card_pic);
                    if(bitmap == null){
                        Log.d("tag","true");
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                    rlCard.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    rlCard.setDrawingCacheEnabled(false);
                }
                doEdit(card_pic);
                break;
            case R.id.llChangeLayout:
                Intent intent = new Intent(CardEditActivity.this, LayoutSelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
                //onBackPressed();
                break;
            case R.id.rlUsertype:
                if(textSelectd.equals("")){
                    Toast.makeText(CardEditActivity.this,"Please select a attribute first",Toast.LENGTH_SHORT).show();
                }else {
                    if (ivDown.getTag().toString().toLowerCase().equals("down")) {
                        ivDown.setTag("up");
                        ivDown.setImageResource(R.drawable.up_red);
                        rlRecyclerView.setVisibility(View.VISIBLE);
                        setFontAdapter(fontFamilyList);
                    } else if (ivDown.getTag().toString().toLowerCase().equals("up")) {
                        ivDown.setTag("down");
                        ivDown.setImageResource(R.drawable.down_red);
                        rlRecyclerView.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.rlColorHolder:
                if(textSelectd.equals("")){
                    Toast.makeText(CardEditActivity.this,"Please select a attribute first",Toast.LENGTH_SHORT).show();
                }else{
                    GridView gvOrganisation = (GridView) ColorPicker.getColorPicker(this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(gvOrganisation);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Objects.requireNonNull(dialog.getWindow()).setLayout(1000,600);
                    dialog.getWindow().setBackgroundDrawableResource(R.color.white);
                    gvOrganisation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int selectedColor = (int) adapterView.getItemAtPosition(i);
                            switch (textSelectd) {
                                case "organisation":
                                    tvOrganisationName.setTextColor(selectedColor);
                                    rlColorHolder.setBackgroundColor(selectedColor);
                                    organisationColor = "#" + Integer.toHexString(selectedColor).substring(2);
                                    break;
                                case "name":
                                    tvName.setTextColor(selectedColor);
                                    rlColorHolder.setBackgroundColor(selectedColor);
                                    nameColor = "#" + Integer.toHexString(selectedColor).substring(2);
                                    break;
                                case "address":
                                    tvAddress.setTextColor(selectedColor);
                                    rlColorHolder.setBackgroundColor(selectedColor);
                                    addressColor = "#" + Integer.toHexString(selectedColor).substring(2);
                                    ImageViewCompat.setImageTintMode(ivAddress, PorterDuff.Mode.SRC_ATOP);
                                    ImageViewCompat.setImageTintList(ivAddress, ColorStateList.valueOf(Color.parseColor(addressColor)));
                                    break;
                                case "mail":
                                    tvemailAddress.setTextColor(selectedColor);
                                    rlColorHolder.setBackgroundColor(selectedColor);
                                    emailColor = "#" + Integer.toHexString(selectedColor).substring(2);
                                    ImageViewCompat.setImageTintMode(ivMail, PorterDuff.Mode.SRC_ATOP);
                                    ImageViewCompat.setImageTintList(ivMail, ColorStateList.valueOf(Color.parseColor(emailColor)));
                                    break;
                                case "phone":
                                    tvphoneNumber.setTextColor(selectedColor);
                                    rlColorHolder.setBackgroundColor(selectedColor);
                                    phoneColor = "#" + Integer.toHexString(selectedColor).substring(2);
                                    ImageViewCompat.setImageTintMode(ivPhone, PorterDuff.Mode.SRC_ATOP);
                                    ImageViewCompat.setImageTintList(ivPhone, ColorStateList.valueOf(Color.parseColor(phoneColor)));
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.rlBorder:
                GridView gvOrganisation = (GridView) ColorPicker.getColorPicker(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(gvOrganisation);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Objects.requireNonNull(dialog.getWindow()).setLayout(1000,600);
                dialog.getWindow().setBackgroundDrawableResource(R.color.white);
                gvOrganisation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int selectedColor = (int) adapterView.getItemAtPosition(i);
                        rlBorderColorHolder.setBackgroundColor(selectedColor);
                        borderColor = "#" + Integer.toHexString(selectedColor).substring(2);
                        GradientDrawable gd = new GradientDrawable();
                        //gd.setColor(Color.RED);
                        //gd.setCornerRadius(10);
                        gd.setStroke(25, Color.parseColor(borderColor));
                        rlCard.setBackgroundDrawable(gd);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.ivBack:
                if(ivBack.getTag().toString().toLowerCase().equals("back")){
                    myPreference.setLayout("");
                    myPreference.setLayoutID("");
                    onBackPressed();
                }else if(ivBack.getTag().toString().toLowerCase().equals("preview")){
                    llLowerView.setVisibility(View.VISIBLE);
                    llSideView.setVisibility(View.VISIBLE);
                    ivBack.setTag("back");
                    tvOrganisationName.setVisibility(View.VISIBLE);
                    tvName.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.VISIBLE);
                    llPhoneNumber.setVisibility(View.VISIBLE);
                    llMail.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void editDialog(final TextView textView, final String textSelectd) {
        final EditCardDialog editCardDialog=new EditCardDialog(this);
        if(!editCardDialog.isShowing()){
            editCardDialog.show();
            switch (textSelectd){
                case "organisation":
                    editCardDialog.tvHeading.setText("Edit Organisation name");
                    editCardDialog.etTitle.setText(ContainerActivity.getCardResponseDataModel.user_organization_name);
                    editCardDialog.etTitle.setInputType(InputType.TYPE_CLASS_TEXT);
                    editCardDialog.etTitle.setMaxLines(1);
                    break;
                case "name":
                    editCardDialog.tvHeading.setText("Edit name");
                    editCardDialog.etTitle.setText(ContainerActivity.getCardResponseDataModel.user_fname+" "+ ContainerActivity.getCardResponseDataModel.user_lname);
                    editCardDialog.etTitle.setMaxLines(1);
                    editCardDialog.etTitle.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    break;
                case "address":
                    editCardDialog.tvHeading.setText("Edit address");
                    editCardDialog.etTitle.setText(ContainerActivity.getCardResponseDataModel.user_address);
                    editCardDialog.etTitle.setMaxLines(4);
                    editCardDialog.etTitle.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    break;
                case "mail":
                    editCardDialog.tvHeading.setText("Edit email address");
                    editCardDialog.etTitle.setText(ContainerActivity.getCardResponseDataModel.user_email);
                    editCardDialog.etTitle.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    editCardDialog.etTitle.setMaxLines(1);
                    break;
            }
            editCardDialog.btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editCardDialog.dismiss();
                }
            });
            editCardDialog.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Objects.requireNonNull(editCardDialog.etTitle.getText()).toString().isEmpty()){
                        Toast.makeText(CardEditActivity.this,"Field cann't be blank.",Toast.LENGTH_SHORT).show();
                    }else{
                       editCardDialog.dismiss();
                        switch (textSelectd){
                            case "organisation":
                                StringBuilder sb = new StringBuilder(editCardDialog.etTitle.getText().toString());
                                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                                organisationName=sb.toString();
                                textView.setText(organisationName);
                                break;
                            case "name":
                                textView.setText(editCardDialog.etTitle.getText().toString());
                                break;
                            case "address":
                                textView.setText(editCardDialog.etTitle.getText().toString()+"-"+ContainerActivity.getCardResponseDataModel.user_pin);
                                break;
                            case "mail":
                                textView.setText(editCardDialog.etTitle.getText().toString());
                                break;
                        }
                    }
                }
            });
        }
    }

    private void doEdit(File card_pic) {
        showRotateDialog();
        int marginleftOragnisation= (int) tvOrganisationName.getX();
        int margintopOragnisation= (int) tvOrganisationName.getY();
        int marginLeftName=(int) tvName.getX();
        int marginTopName=(int) tvName.getY();
        int marginLeftAddress=(int) llAddress.getX();
        int marginBottomAddress=(int) llAddress.getY();
        int marginRightMail=(int) llMail.getX();
        int marginBottomMail=(int) llMail.getY();
        int marginRightPhone=(int) llPhoneNumber.getX();
        int marginBottomPhone=(int) llPhoneNumber.getY();
        RequestBody apiUser=RequestBody.create(MediaType.parse("multipart/form-data"), Constant.apiuser);
        RequestBody apiPass=RequestBody.create(MediaType.parse("multipart/form-data"), Constant.apipass);
        RequestBody userId=RequestBody.create(MediaType.parse("multipart/form-data"), myPreference.getUserID());
        RequestBody layoutid=RequestBody.create(MediaType.parse("multipart/form-data"), layoutId);
        RequestBody orgcolor=RequestBody.create(MediaType.parse("multipart/form-data"), organisationColor);
        RequestBody orgfont=null;
        if(organisationFont != null)
            orgfont= RequestBody.create(MediaType.parse("multipart/form-data"),organisationFont);
        else
            orgfont= RequestBody.create(MediaType.parse("multipart/form-data"),"");
        RequestBody orgtop=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(margintopOragnisation));
        RequestBody orgleft=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginleftOragnisation));
        RequestBody orgvisibility = null;
        if(oraganisationVisibility){
            orgvisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        }else {
            orgvisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        }
        RequestBody namecolor=RequestBody.create(MediaType.parse("multipart/form-data"), nameColor);
        RequestBody namefont=null;
        if(nameFont != null)
            namefont= RequestBody.create(MediaType.parse("multipart/form-data"),nameFont);
        else
            namefont= RequestBody.create(MediaType.parse("multipart/form-data"),"");
        RequestBody nametop=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginTopName));
        RequestBody nameleft=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginLeftName));
        RequestBody namevisibility = null;
        if(nameVisibility){
            namevisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        }else {
            namevisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        }
        RequestBody addcolor=RequestBody.create(MediaType.parse("multipart/form-data"), addressColor);
        RequestBody addfont=null;
        if(addressFont != null)
            addfont= RequestBody.create(MediaType.parse("multipart/form-data"),addressFont);
        else
            addfont= RequestBody.create(MediaType.parse("multipart/form-data"),"");
        RequestBody addtop=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginBottomAddress));
        RequestBody addleft=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginLeftAddress));
        RequestBody addvisibility = null;
        if(addressVisibility){
            addvisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        }else {
            addvisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        }
        RequestBody emailcolor=RequestBody.create(MediaType.parse("multipart/form-data"), emailColor);
        RequestBody emailfont=null;
        if(emailfont != null)
            emailfont= RequestBody.create(MediaType.parse("multipart/form-data"),emailFont);
        else
            emailfont= RequestBody.create(MediaType.parse("multipart/form-data"),"");
        RequestBody emailtop=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginBottomMail));
        RequestBody emailleft=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginRightMail));
        RequestBody emailvisibility = null;
        if(emailVisibility){
            emailvisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        }else {
            emailvisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        }
        RequestBody phonecolor=RequestBody.create(MediaType.parse("multipart/form-data"), phoneColor);
        RequestBody phonefont=null;
        if(phoneFont != null)
            phonefont= RequestBody.create(MediaType.parse("multipart/form-data"),phoneFont);
        else
           phonefont= RequestBody.create(MediaType.parse("multipart/form-data"),"");
        RequestBody phonetop=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginBottomPhone));
        RequestBody phoneleft=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(marginRightPhone));
        RequestBody phonevisibility = null;
        if(emailVisibility){
            phonevisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        }else {
            phonevisibility=RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        }
        RequestBody userfname=null;
        RequestBody userlName=null;
        userfname=RequestBody.create(MediaType.parse("multipart/form-data"), tvName.getText().toString());
        userlName=RequestBody.create(MediaType.parse("multipart/form-data"), "");
        String[] addressArray=tvAddress.getText().toString().split("-");
        RequestBody address=RequestBody.create(MediaType.parse("multipart/form-data"),addressArray[0]);
        RequestBody email=RequestBody.create(MediaType.parse("multipart/form-data"),tvemailAddress.getText().toString());
        RequestBody bordercolor=RequestBody.create(MediaType.parse("multipart/form-data"),borderColor);
        StringBuilder sb = new StringBuilder(tvOrganisationName.getText().toString());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        organisationName=sb.toString();
        RequestBody org=RequestBody.create(MediaType.parse("multipart/form-data"),organisationName);
        RequestBody phoneFont = null;
        if(phoneFontvalue != null){
            phoneFont=RequestBody.create(MediaType.parse("multipart/form-data"), phoneFontvalue);
        }else {
            phoneFont=RequestBody.create(MediaType.parse("multipart/form-data"), cardDetailsResponseModel.card_phone_fontsize_mob);
        }
        RequestBody nameFont = null;
        if(nameFontValue != null){
            nameFont=RequestBody.create(MediaType.parse("multipart/form-data"), nameFontValue);
        }else {
            nameFont=RequestBody.create(MediaType.parse("multipart/form-data"), cardDetailsResponseModel.card_name_fontsize_mob);
        }
        RequestBody addressFont = null;
        if(addressFontValue != null){
            addressFont=RequestBody.create(MediaType.parse("multipart/form-data"), addressFontValue);
        }else {
            addressFont=RequestBody.create(MediaType.parse("multipart/form-data"), cardDetailsResponseModel.card_address_fontsize_mob);
        }
        RequestBody emailFont = null;
        if(emailFontvalue != null){
            emailFont=RequestBody.create(MediaType.parse("multipart/form-data"), emailFontvalue);
        }else {
            emailFont=RequestBody.create(MediaType.parse("multipart/form-data"), cardDetailsResponseModel.card_email_fontsize_mob);
        }
        RequestBody orgFont = null;
        if(organisationFontvalue != null){
            orgFont=RequestBody.create(MediaType.parse("multipart/form-data"), organisationFontvalue);
        }else {
            orgFont=RequestBody.create(MediaType.parse("multipart/form-data"), cardDetailsResponseModel.card_org_fontsize_mob);
        }
        MultipartBody.Part image = null;
        if (card_pic != null) {
            RequestBody propertyImage = RequestBody.create(MediaType.parse("multipart/form-data"), card_pic);
            image = MultipartBody.Part.createFormData("card_image", "card.png", propertyImage);
        }
        Call<ResponseBody> doEdit= RestManager.getInstance().getService().edit_card(apiUser,apiPass,userId,layoutid,orgcolor,orgfont,orgtop,orgleft,orgvisibility,namecolor,namefont,nametop,nameleft,namevisibility,addcolor,addfont,addtop,addleft,addvisibility,emailcolor,emailfont,emailtop,emailleft,emailvisibility,phonecolor,phonefont,phonetop,phoneleft,phonevisibility,userfname,userlName,address,email,org,bordercolor,phoneFont,addressFont,emailFont,nameFont,orgFont,image);
        doEdit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                try{
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile();
                    }else if(jsonObject.optInt("code") == 9){
                        hideRotateDialog();
                        customAlert("Authentication error occurred.");
                    }else{
                        hideRotateDialog();
                        customAlert("Oops, something went wrong!");
                    }
                }catch(Exception e){
                    hideRotateDialog();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call,@NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private void getProfile() {
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        GetCardClass getCardClass=new GetCardClass();
        getCardClass.apiCredentialModel=apiCredentialModel;
        getCardClass.user_id=myPreference.getUserID();
        getCardClass.logged_in_user_id=myPreference.getUserID();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(getCardClass);
        Call<GetCardResponseModel> getprofile= RestManager.getInstance().getService().get_profile(jsonElement);
        getprofile.enqueue(new Callback<GetCardResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<GetCardResponseModel> call, @NotNull Response<GetCardResponseModel> response) {
                hideRotateDialog();
                try{
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                        ContainerActivity.galleryList.clear();
                        ContainerActivity.layoutList.clear();
                        ContainerActivity.youtubeDetailsModelArrayList.clear();
                        ContainerActivity.layoutUrl=response.body().layout_url;
                        ContainerActivity.viewCount=response.body().view_count;
                        ContainerActivity.galleryList=response.body().gallery_details;
                        cardDetailsResponseModel=response.body().card_details;
                        ContainerActivity.layoutList=response.body().all_layouts;
                        ContainerActivity.youtubeDetailsModelArrayList=response.body().youtube_details;
                        ContainerActivity.getCardResponseDataModel=response.body().user_details;
                        ContainerActivity.follow_status=response.body().follow_status;
                        ContainerActivity.follow_count=response.body().no_of_followers;
                        ContainerActivity.validity_status=response.body().validity_status;
                        ContainerActivity.validityDate=response.body().user_details.user_card_valid_until;
                        customAlertForSave("Your card has been edited successfully.");
                        //onBackPressed();
                    }else if(code == 9){
                        customAlert("An authentication error occured!");
                    }else{
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCardResponseModel> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });

    }

    private void customAlertForSave(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(this);
        if(!customAlertWithOneButton.isShowing()){
            customAlertWithOneButton.show();
            customAlertWithOneButton.setCanceledOnTouchOutside(false);
            customAlertWithOneButton.setCancelable(false);
            customAlertWithOneButton.tvDesc.setText(s);
            customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithOneButton.dismiss();
                    myPreference.setLayout("");
                    myPreference.setLayoutID("");
                    myPreference.setFromCardEdit(true);
                    onBackPressed();
                }
            });
        }
    }

    private void setFontAdapter(ArrayList<String> fontFamilyList) {
        if(fontFamilyList.size() > 0){
            rvFont.setVisibility(View.VISIBLE);
            rvFont.setLayoutManager(new LinearLayoutManager(this));
            tvEmptyText.setVisibility(View.GONE);
            switch (textSelectd) {
                case "organisation": {
                    FontFamilyAdapter fontFamilyAdapter = new FontFamilyAdapter(tvAttributeFont,fontFamilyList,this, rlRecyclerView, ivDown, organisationFont, organistionTypeface, tvOrganisationName);
                    rvFont.setAdapter(fontFamilyAdapter);
                    break;
                }
                case "name": {
                    FontFamilyAdapter fontFamilyAdapter = new FontFamilyAdapter(tvAttributeFont,fontFamilyList,this, rlRecyclerView, ivDown, addressFont, addressTypeface, tvName);
                    rvFont.setAdapter(fontFamilyAdapter);
                    break;
                }
                case "address": {
                    FontFamilyAdapter fontFamilyAdapter = new FontFamilyAdapter(tvAttributeFont,fontFamilyList,this, rlRecyclerView, ivDown, addressFont, addressTypeface, tvAddress);
                    rvFont.setAdapter(fontFamilyAdapter);
                    break;
                }
                case "mail": {
                    FontFamilyAdapter fontFamilyAdapter = new FontFamilyAdapter(tvAttributeFont,fontFamilyList,this, rlRecyclerView, ivDown, emailFont, emailtypeface, tvemailAddress);
                    rvFont.setAdapter(fontFamilyAdapter);
                    break;
                }
                case "phone": {
                    FontFamilyAdapter fontFamilyAdapter = new FontFamilyAdapter(tvAttributeFont,fontFamilyList,this, rlRecyclerView, ivDown, phoneFont, phoneTypeface, tvphoneNumber);
                    rvFont.setAdapter(fontFamilyAdapter);
                    break;
                }
            }
        }else{
            rvFont.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(this);
        if(!customAlertWithOneButton.isShowing()){
            customAlertWithOneButton.show();
            customAlertWithOneButton.setCanceledOnTouchOutside(false);
            customAlertWithOneButton.setCancelable(false);
            customAlertWithOneButton.tvDesc.setText(s);
            customAlertWithOneButton.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithOneButton.dismiss();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotateDialog != null && rotateDialog.isShowing()) {
            rotateDialog.dismiss();
        }
        rotateDialog = null;
    }

}
