package com.app.wokk.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.adapter.CardGalleryAdapter;
import com.app.wokk.adapter.CardYoutubeAdapter;
import com.app.wokk.adapter.GalleryAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.AllLayoutsResponseModel;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.CardDetailsResponseModel;
import com.app.wokk.model.FollowClass;
import com.app.wokk.model.GalleryModel;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseDataModel;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.YoutubeDetailsModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.wokk.activity.ContainerActivity.tvViews;

public class MycardFragment extends BaseFragment implements View.OnClickListener {

    public static MycardFragment newInstance() {
        return new MycardFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public RecyclerView rvGallery, rvYoutube;
    public EditText etNumber;
    public RelativeLayout rlSms, rlWhatsappShare, rlFollow, rlWhatsapp, rlShare, rlCall, rlWokkYoutubeLink, rlCard, rlYoutube, rlAlert;
    public TextView tvOrganisationName, tvName, tvWokkYoutubeLink, tvCardAddress, tvemailAddress, tvphoneNumber, tvEmptyText, tvFollowed, tvEmptyTextYoutube, tvProfile;
    public LinearLayout llAddress, llMail, llPhoneNumber;
    public ImageView ivInstagram, ivcard, ivTwitter, ivFollowed, ivFacebook, ivContact, ivAddress, ivMail, ivPhone;
    public GetCardResponseDataModel getCardResponseDataModel;
    public CardDetailsResponseModel cardDetailsResponseModel;
    public ArrayList<GalleryResponseModel> galleryCardList;
    public ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList;
    public ArrayList<AllLayoutsResponseModel> layoutList;
    public int follow_status;
    NestedScrollView scroll;
    public boolean validity_status;
    public String validityDate;
    public String follow_count, addressColor, emailColor, phoneColor, viewCount, layoutUrl;
    Typeface organistionTypeface, nameTypeface, addressTypeface, emailtypeface, phoneTypeface;
    String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_my_card, container, false);
        myPreference = new MyPreference(getActivity());
        init(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean networkCheck = NetworkCheck.getInstant(getActivity()).isConnectingToInternet();
        if (networkCheck) {
            getUserdetails();
        } else {
            customAlert(getResources().getString(R.string.noInternetText));
        }
    }

    private void getUserdetails() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel = new ApiCredentialModel();
        apiCredentialModel.apiuser = Constant.apiuser;
        apiCredentialModel.apipass = Constant.apipass;
        GetCardClass getCardClass = new GetCardClass();
        getCardClass.apiCredentialModel = apiCredentialModel;
        getCardClass.user_id = myPreference.getServiceUserId();
        getCardClass.logged_in_user_id = myPreference.getUserID();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(getCardClass);
        Call<GetCardResponseModel> getprofile = RestManager.getInstance().getService().get_profile(jsonElement);
        getprofile.enqueue(new Callback<GetCardResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<GetCardResponseModel> call, @NotNull Response<GetCardResponseModel> response) {
                hideRotateDialog();
                try {
                    assert response.body() != null;
                    int code = response.body().code;
                    if (code == 1) {
                        layoutUrl = response.body().layout_url;
                        cardDetailsResponseModel = response.body().card_details;
                        getCardResponseDataModel = response.body().user_details;
                        follow_status = response.body().follow_status;
                        follow_count = response.body().no_of_followers;
                        galleryCardList = new ArrayList<>();
                        youtubeDetailsModelArrayList = new ArrayList<>();
                        layoutList = new ArrayList<>();
                        galleryCardList.clear();
                        layoutList.clear();
                        youtubeDetailsModelArrayList.clear();
                        galleryCardList = response.body().gallery_details;
                        layoutList = response.body().all_layouts;
                        youtubeDetailsModelArrayList = response.body().youtube_details;
                        viewCount = response.body().view_count;
                        validity_status = response.body().validity_status;
                        validityDate = response.body().user_details.user_card_valid_until;
                        /*if(viewCount != null)
                            tvViews.setText("Views: "+viewCount);
                        else
                            tvViews.setText("Views: 0");*/
                        setData();
                        setupAdapter(galleryCardList);
                        setUpYoutubeAdapter(youtubeDetailsModelArrayList);
                    } else if (code == 9) {
                        customAlert("An authentication error occured!");
                    } else {
                        customAlert("Oops, something went wrong!");
                    }
                } catch (Exception e) {
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

    private void setData() {
        if (follow_count == null) {
            if (follow_status == 0) {
                tvFollowed.setText("Follow");
                ivFollowed.setVisibility(View.VISIBLE);
                tvFollowed.setTextSize(getResources().getDimensionPixelSize(R.dimen._4sdp));
                rlFollow.setAlpha(1.0f);
                rlCall.setAlpha(1.0f);
                rlFollow.setClickable(true);
                rlCall.setClickable(true);
                rlCall.setEnabled(true);
                rlFollow.setEnabled(true);
                rlFollow.setFocusable(true);
                rlCall.setFocusable(true);
            } else if (follow_status == 1) {
                tvFollowed.setText("Followed");
                ivFollowed.setVisibility(View.GONE);
                tvFollowed.setTextSize(getResources().getDimensionPixelSize(R.dimen._4sdp));
                rlFollow.setAlpha(0.4f);
                rlFollow.setAlpha(1.0f);
                rlCall.setAlpha(1.0f);
                rlFollow.setClickable(false);
                rlCall.setClickable(true);
                rlCall.setEnabled(true);
                rlFollow.setEnabled(false);
                rlFollow.setFocusable(false);
                rlCall.setFocusable(true);
            }
        } else {
            tvFollowed.setText("Followers: " + follow_count);
            ivFollowed.setVisibility(View.GONE);
            //rlFollow.setAlpha(0.4f);
            tvFollowed.setTextSize(getResources().getDimensionPixelSize(R.dimen._3sdp));
            rlCall.setAlpha(0.4f);
            rlFollow.setClickable(false);
            rlCall.setClickable(false);
            rlCall.setEnabled(false);
            rlFollow.setEnabled(false);
            rlFollow.setFocusable(false);
        }
        if (validity_status) {
            scroll.setVisibility(View.VISIBLE);
            rlAlert.setVisibility(View.GONE);
            /*GradientDrawable gd = new GradientDrawable();
            //gd.setColor(Color.RED);
            //gd.setCornerRadius(10);
            gd.setStroke(10, Color.parseColor(cardDetailsResponseModel.card_border_color));*/
            ivcard.setBackgroundColor(Color.parseColor(cardDetailsResponseModel.card_border_color));
            Glide.with(Objects.requireNonNull(getActivity())).load(layoutUrl + cardDetailsResponseModel.layout_image).into(ivcard);
            //new LoadBackground(layoutUrl + cardDetailsResponseModel.layout_image, "androidfigure").execute();
            if (getCardResponseDataModel.user_fname != null && getCardResponseDataModel.user_lname != null)
                tvName.setText(getCardResponseDataModel.user_fname + " " + getCardResponseDataModel.user_lname);
            if (getCardResponseDataModel.user_address != null && getCardResponseDataModel.user_pin != null)
                tvCardAddress.setText(getCardResponseDataModel.user_address + " - " + getCardResponseDataModel.user_pin);
            if (getCardResponseDataModel.user_phone != null)
                tvphoneNumber.setText(getCardResponseDataModel.user_phone);
            if (getCardResponseDataModel.user_organization_name != null)
                tvOrganisationName.setText(getCardResponseDataModel.user_organization_name);
            if (getCardResponseDataModel.user_email != null)
                tvemailAddress.setText(getCardResponseDataModel.user_email);
            RelativeLayout.LayoutParams tvNameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
            if (cardDetailsResponseModel.card_name_top_mob != null)
                tvNameParams.topMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_name_top_mob));
            else
                tvNameParams.topMargin = 0;
            if (cardDetailsResponseModel.card_name_left_mob != null)
                tvNameParams.leftMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_name_left_mob));
            else
                tvNameParams.leftMargin = 0;
            tvName.setLayoutParams(tvNameParams);
            RelativeLayout.LayoutParams tvOrganisationParams = (RelativeLayout.LayoutParams) tvOrganisationName.getLayoutParams();
            if (cardDetailsResponseModel.card_org_top_mob != null)
                tvOrganisationParams.topMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_org_top_mob));
            else
                tvOrganisationParams.topMargin = 0;
            if (cardDetailsResponseModel.card_org_left_mob != null)
                tvOrganisationParams.leftMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_org_left_mob));
            else
                tvOrganisationParams.leftMargin = 0;
            tvOrganisationName.setLayoutParams(tvOrganisationParams);
            RelativeLayout.LayoutParams addressParams = (RelativeLayout.LayoutParams) llAddress.getLayoutParams();
            if (cardDetailsResponseModel.card_address_top_mob != null)
                addressParams.topMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_address_top_mob));
            else
                addressParams.topMargin = 0;
            if (cardDetailsResponseModel.card_address_left_mob != null)
                addressParams.leftMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_address_left_mob));
            else
                addressParams.leftMargin = 0;
            llAddress.setLayoutParams(addressParams);
            RelativeLayout.LayoutParams phoneParams = (RelativeLayout.LayoutParams) llPhoneNumber.getLayoutParams();
            if (cardDetailsResponseModel.card_phone_top_mob != null)
                phoneParams.topMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_phone_top_mob));
            else
                phoneParams.topMargin = 0;
            if (cardDetailsResponseModel.card_phone_left_mob != null)
                phoneParams.leftMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_phone_left_mob));
            else
                phoneParams.leftMargin = 0;
            llPhoneNumber.setLayoutParams(phoneParams);
            RelativeLayout.LayoutParams mailParams = (RelativeLayout.LayoutParams) llMail.getLayoutParams();
            if (cardDetailsResponseModel.card_email_top_mob != null)
                mailParams.topMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_email_top_mob));
            else
                mailParams.topMargin = 0;
            if (cardDetailsResponseModel.card_email_left_mob != null)
                mailParams.leftMargin = Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_email_left_mob));
            else
                mailParams.leftMargin = 0;
            llMail.setLayoutParams(mailParams);
            if (cardDetailsResponseModel.card_org_color.contains("0") && cardDetailsResponseModel.card_org_color.length() == 4) {
                tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color + "000"));
            } else if (cardDetailsResponseModel.card_org_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_org_color.length() == 4) {
                tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color.toLowerCase() + "fff"));
            } else {
                tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color));
            }
            if (cardDetailsResponseModel.card_name_color.contains("0") && cardDetailsResponseModel.card_name_color.length() == 4) {
                tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color + "000"));
            } else if (cardDetailsResponseModel.card_name_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_name_color.length() == 4) {
                tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color.toLowerCase() + "fff"));
            } else {
                tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color));
            }
            if (cardDetailsResponseModel.card_address_color.contains("0") && cardDetailsResponseModel.card_address_color.length() == 4) {
                tvCardAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color + "000"));
                addressColor = cardDetailsResponseModel.card_address_color + "000";
            } else if (cardDetailsResponseModel.card_address_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_address_color.length() == 4) {
                tvCardAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color.toLowerCase() + "fff"));
                addressColor = cardDetailsResponseModel.card_address_color + "fff";
            } else {
                tvCardAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color));
                addressColor = cardDetailsResponseModel.card_address_color;
            }
            ImageViewCompat.setImageTintMode(ivAddress, PorterDuff.Mode.SRC_ATOP);
            ImageViewCompat.setImageTintList(ivAddress, ColorStateList.valueOf(Color.parseColor(addressColor)));
            if (cardDetailsResponseModel.card_email_color.contains("0") && cardDetailsResponseModel.card_email_color.length() == 4) {
                tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color + "000"));
                emailColor = cardDetailsResponseModel.card_email_color + "000";
            } else if (cardDetailsResponseModel.card_email_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_email_color.length() == 4) {
                tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color.toLowerCase() + "fff"));
                emailColor = cardDetailsResponseModel.card_email_color + "fff";
            } else {
                tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color));
                emailColor = cardDetailsResponseModel.card_email_color;
            }
            ImageViewCompat.setImageTintMode(ivMail, PorterDuff.Mode.SRC_ATOP);
            ImageViewCompat.setImageTintList(ivMail, ColorStateList.valueOf(Color.parseColor(emailColor)));
            if (cardDetailsResponseModel.card_phone_color.contains("0") && cardDetailsResponseModel.card_phone_color.length() == 4) {
                tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color + "000"));
                phoneColor = cardDetailsResponseModel.card_phone_color + "000";
            } else if (cardDetailsResponseModel.card_phone_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_phone_color.length() == 4) {
                tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color.toLowerCase() + "fff"));
                phoneColor = cardDetailsResponseModel.card_phone_color + "fff";
            } else {
                tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color));
                phoneColor = cardDetailsResponseModel.card_phone_color;
            }
            ImageViewCompat.setImageTintMode(ivPhone, PorterDuff.Mode.SRC_ATOP);
            ImageViewCompat.setImageTintList(ivPhone, ColorStateList.valueOf(Color.parseColor(phoneColor)));
            tvOrganisationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_org_fontsize_mob));
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_name_fontsize_mob));
            tvphoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_phone_fontsize_mob));
            tvCardAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_address_fontsize_mob));
            tvemailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_email_fontsize_mob));
            if (cardDetailsResponseModel.card_org_show.equals("0")) {
                tvOrganisationName.setVisibility(View.GONE);
            } else {
                tvOrganisationName.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_name_show.equals("0")) {
                tvName.setVisibility(View.GONE);
            } else {
                tvName.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_address_show.equals("0")) {
                llAddress.setVisibility(View.GONE);
            } else {
                llAddress.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_email_show.equals("0")) {
                llMail.setVisibility(View.GONE);
            } else {
                llMail.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_phone_show.equals("0")) {
                llPhoneNumber.setVisibility(View.GONE);
                rlCall.setAlpha(0.4f);
                rlCall.setClickable(false);
                rlCall.setEnabled(false);
            } else {
                llPhoneNumber.setVisibility(View.VISIBLE);
                rlCall.setAlpha(1.0f);
                rlCall.setClickable(true);
                rlCall.setEnabled(true);
            }
            if (cardDetailsResponseModel.card_org_font != null) {
                if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("courier new, monospace")) {
                    organistionTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("inconsolata")) {
                    phoneTypeface = getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("recursive")) {
                    organistionTypeface = getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("cedarville cursive")) {
                    organistionTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("noto sans")) {
                    organistionTypeface = getResources().getFont(R.font.notosans_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("poppins")) {
                    organistionTypeface = getResources().getFont(R.font.poppins_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("open sans")) {
                    organistionTypeface = getResources().getFont(R.font.opensans_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("roboto")) {
                    organistionTypeface = getResources().getFont(R.font.roboto_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("montserrat")) {
                    organistionTypeface = getResources().getFont(R.font.montserrat_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("lato")) {
                    organistionTypeface = getResources().getFont(R.font.lato_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("source sans pro")) {
                    organistionTypeface = getResources().getFont(R.font.sourcesanspro_regular);
                    tvOrganisationName.setTypeface(organistionTypeface);
                } else if (cardDetailsResponseModel.card_org_font.toLowerCase().equals("raleway, sans-serif")) {
                    organistionTypeface = getResources().getFont(R.font.raleway_variablefont_wght);
                    tvOrganisationName.setTypeface(organistionTypeface);
                }
                //organisationFont=cardDetailsResponseModel.card_org_font;
            }
            if (cardDetailsResponseModel.card_name_font != null) {
                if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("courier new, monospace")) {
                    nameTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("inconsolata")) {
                    nameTypeface = getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("recursive")) {
                    nameTypeface = getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("cedarville cursive")) {
                    nameTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("noto sans")) {
                    nameTypeface = getResources().getFont(R.font.notosans_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("poppins")) {
                    nameTypeface = getResources().getFont(R.font.poppins_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("open sans")) {
                    nameTypeface = getResources().getFont(R.font.opensans_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("roboto")) {
                    nameTypeface = getResources().getFont(R.font.roboto_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("montserrat")) {
                    nameTypeface = getResources().getFont(R.font.montserrat_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("lato")) {
                    nameTypeface = getResources().getFont(R.font.lato_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("source sans pro")) {
                    nameTypeface = getResources().getFont(R.font.sourcesanspro_regular);
                    tvName.setTypeface(nameTypeface);
                } else if (cardDetailsResponseModel.card_name_font.toLowerCase().equals("raleway, sans-serif")) {
                    nameTypeface = getResources().getFont(R.font.raleway_variablefont_wght);
                    tvName.setTypeface(nameTypeface);
                }
                //nameFont=cardDetailsResponseModel.card_name_font;
            }
            if (cardDetailsResponseModel.card_email_font != null) {
                if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("courier new, monospace")) {
                    emailtypeface = getResources().getFont(R.font.courierprime_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("inconsolata")) {
                    emailtypeface = getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("recursive")) {
                    emailtypeface = getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("cedarville cursive")) {
                    emailtypeface = getResources().getFont(R.font.courierprime_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("noto sans")) {
                    emailtypeface = getResources().getFont(R.font.notosans_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("poppins")) {
                    emailtypeface = getResources().getFont(R.font.poppins_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("open sans")) {
                    emailtypeface = getResources().getFont(R.font.opensans_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("roboto")) {
                    emailtypeface = getResources().getFont(R.font.roboto_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("montserrat")) {
                    emailtypeface = getResources().getFont(R.font.montserrat_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("lato")) {
                    emailtypeface = getResources().getFont(R.font.lato_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("source sans pro")) {
                    emailtypeface = getResources().getFont(R.font.sourcesanspro_regular);
                    tvemailAddress.setTypeface(emailtypeface);
                } else if (cardDetailsResponseModel.card_email_font.toLowerCase().equals("raleway, sans-serif")) {
                    emailtypeface = getResources().getFont(R.font.raleway_variablefont_wght);
                    tvemailAddress.setTypeface(emailtypeface);
                }
                //emailFont=cardDetailsResponseModel.card_email_font;
            }
            if (cardDetailsResponseModel.card_address_font != null) {
                if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("courier new, monospace")) {
                    addressTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("inconsolata")) {
                    addressTypeface = getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("recursive")) {
                    addressTypeface = getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("cedarville cursive")) {
                    addressTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("noto sans")) {
                    addressTypeface = getResources().getFont(R.font.notosans_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("poppins")) {
                    addressTypeface = getResources().getFont(R.font.poppins_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("open sans")) {
                    addressTypeface = getResources().getFont(R.font.opensans_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("roboto")) {
                    addressTypeface = getResources().getFont(R.font.roboto_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("montserrat")) {
                    addressTypeface = getResources().getFont(R.font.montserrat_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("lato")) {
                    addressTypeface = getResources().getFont(R.font.lato_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("source sans pro")) {
                    addressTypeface = getResources().getFont(R.font.sourcesanspro_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                } else if (cardDetailsResponseModel.card_address_font.toLowerCase().equals("raleway, sans-serif")) {
                    addressTypeface = getResources().getFont(R.font.raleway_variablefont_wght);
                    tvCardAddress.setTypeface(addressTypeface);
                }
                //addressFont=cardDetailsResponseModel.card_address_font;
            }
            if (cardDetailsResponseModel.card_phone_font != null) {
                if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("courier new, monospace")) {
                    phoneTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("inconsolata")) {
                    phoneTypeface = getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("recursive")) {
                    phoneTypeface = getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("cedarville cursive")) {
                    phoneTypeface = getResources().getFont(R.font.courierprime_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("noto sans")) {
                    phoneTypeface = getResources().getFont(R.font.notosans_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("poppins")) {
                    phoneTypeface = getResources().getFont(R.font.poppins_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("open sans")) {
                    phoneTypeface = getResources().getFont(R.font.opensans_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("roboto")) {
                    phoneTypeface = getResources().getFont(R.font.roboto_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("montserrat")) {
                    phoneTypeface = getResources().getFont(R.font.montserrat_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("lato")) {
                    phoneTypeface = getResources().getFont(R.font.lato_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("source sans pro")) {
                    phoneTypeface = getResources().getFont(R.font.sourcesanspro_regular);
                    tvphoneNumber.setTypeface(phoneTypeface);
                } else if (cardDetailsResponseModel.card_phone_font.toLowerCase().equals("raleway, sans-serif")) {
                    phoneTypeface = getResources().getFont(R.font.raleway_variablefont_wght);
                    tvphoneNumber.setTypeface(phoneTypeface);
                }
                //phoneFont=cardDetailsResponseModel.card_phone_font;
            }
        } else {
            scroll.setVisibility(View.GONE);
            rlAlert.setVisibility(View.VISIBLE);
        }
    }

    private void init(View rootView) {
        rlAlert = rootView.findViewById(R.id.rlAlert);
        scroll = rootView.findViewById(R.id.scroll);
        tvProfile = rootView.findViewById(R.id.tvProfile);
        ivPhone = rootView.findViewById(R.id.ivPhone);
        tvEmptyTextYoutube = rootView.findViewById(R.id.tvEmptyTextYoutube);
        rvYoutube = rootView.findViewById(R.id.rvYoutube);
        rlYoutube = rootView.findViewById(R.id.rlYoutube);
        ivMail = rootView.findViewById(R.id.ivMail);
        ivAddress = rootView.findViewById(R.id.ivAddress);
        ivcard = rootView.findViewById(R.id.ivcard);
        tvFollowed = rootView.findViewById(R.id.tvFollowed);
        ivFacebook = rootView.findViewById(R.id.ivFirstStar);
        ivTwitter = rootView.findViewById(R.id.ivTwitter);
        ivInstagram = rootView.findViewById(R.id.ivInstagram);
        ivContact = rootView.findViewById(R.id.ivContact);
        ivFollowed = rootView.findViewById(R.id.ivFollowed);
        llPhoneNumber = rootView.findViewById(R.id.llPhoneNumber);
        llMail = rootView.findViewById(R.id.llMail);
        llAddress = rootView.findViewById(R.id.llAddress);
        tvEmptyText = rootView.findViewById(R.id.tvEmptyText);
        tvphoneNumber = rootView.findViewById(R.id.tvphoneNumber);
        tvemailAddress = rootView.findViewById(R.id.tvemailAddress);
        tvCardAddress = rootView.findViewById(R.id.tvCardAddress);
        tvWokkYoutubeLink = rootView.findViewById(R.id.tvWokkYoutubeLink);
        tvName = rootView.findViewById(R.id.tvName);
        tvOrganisationName = rootView.findViewById(R.id.tvOrganisationName);
       // rlCard = rootView.findViewById(R.id.rlCard);
        rlWokkYoutubeLink = rootView.findViewById(R.id.rlWokkYoutubeLink);
        rlCall = rootView.findViewById(R.id.rlCall);
        rlShare = rootView.findViewById(R.id.rlShare);
        rlWhatsapp = rootView.findViewById(R.id.rlWhatsapp);
        rlFollow = rootView.findViewById(R.id.rlFollow);
        rlWhatsappShare = rootView.findViewById(R.id.rlWhatsappShare);
        rlSms = rootView.findViewById(R.id.rlSms);
        etNumber = rootView.findViewById(R.id.etNumber);
        rvGallery = rootView.findViewById(R.id.rvGallery);
        clickEvent();
    }

    private void clickEvent() {
        rlWhatsapp.setOnClickListener(this);
        rlWhatsappShare.setOnClickListener(this);
        rlFollow.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlCall.setOnClickListener(this);
        rlSms.setOnClickListener(this);
        rlWokkYoutubeLink.setOnClickListener(this);
        tvProfile.setOnClickListener(this);
        ivContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvProfile:
                Fragment profileFragment = ProfileFragment.newInstance();
                FragmentTransaction profileTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                profileTransaction.replace(R.id.frameContainerChild, profileFragment);
                profileTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                profileTransaction.addToBackStack(null);
                profileTransaction.commit();
                break;
            case R.id.rlWhatsapp:
                if (myPreference.getUserID().equals(myPreference.getServiceUserId())) {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra(Intent.EXTRA_TEXT, "https://wokk.co.in/card/" + getCardResponseDataModel.user_token);
                    startActivity(Intent.createChooser(waIntent, "Share visiting card with"));
                } else {
                    String number = "+91" + getCardResponseDataModel.user_phone;
                    String url = "https://api.whatsapp.com/send?phone=" + number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                break;
            case R.id.rlWhatsappShare:
                hideKeyBoardRelativeLayout(rlWhatsappShare);
                if (etNumber.getText().toString().isEmpty()) {
                    customAlert("Please enter a mobile number to continue!");
                } else {
                    PackageManager packageManager = Objects.requireNonNull(getContext()).getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    try {
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + etNumber.getText().toString() + "&text=" + URLEncoder.encode("https://wokk.co.in/card/" + getCardResponseDataModel.user_token, "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            getContext().startActivity(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.rlFollow:
                hideKeyBoardRelativeLayout(rlFollow);
                doFollow();
                break;
            case R.id.rlShare:
                hideKeyBoardRelativeLayout(rlShare);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My visiting card");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://wokk.co.in/card/" + getCardResponseDataModel.user_token);
                startActivity(Intent.createChooser(sharingIntent, "Share visiting card via"));
                break;
            case R.id.rlCall:
                requestCallPermission();
                break;
            case R.id.rlSms:
                hideKeyBoardRelativeLayout(rlSms);
                if (etNumber.getText().toString().isEmpty()) {
                    customAlert("Please enter a mobile number to continue!");
                } else {
                    requestSmsPermission();
                }
                break;
            case R.id.rlWokkYoutubeLink:
                customAlert("This section is in under development.");
                break;
            case R.id.ivContact:
                hideKeyBoardImageView(ivContact);
                requestContactAccessPermission();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri contactData = data.getData();
                String number = "";
                assert contactData != null;
                Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(contactData, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getActivity().getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    assert phones != null;
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                    }
                    phones.close();
                    //Do something with number
                    etNumber.setText(number);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Contact list is empty.", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

    private void doFollow() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel = new ApiCredentialModel();
        apiCredentialModel.apiuser = Constant.apiuser;
        apiCredentialModel.apipass = Constant.apipass;
        FollowClass followClass = new FollowClass();
        followClass.apiCredentialModel = apiCredentialModel;
        followClass.user_id = myPreference.getServiceUserId();
        followClass.follower_id = myPreference.getUserID();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(followClass);
        Call<ResponseBody> doFollow = RestManager.getInstance().getService().doFollow(jsonElement);
        doFollow.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                hideRotateDialog();
                try {
                    int status = response.code();
                    if (status == 200) {
                        tvFollowed.setText("Followed");
                        ivFollowed.setVisibility(View.GONE);
                        tvFollowed.setTextSize(getResources().getDimensionPixelSize(R.dimen._4sdp));
                        rlFollow.setAlpha(0.4f);
                        rlCall.setAlpha(1.0f);
                        rlFollow.setClickable(false);
                        rlCall.setClickable(true);
                        rlCall.setEnabled(true);
                        rlFollow.setEnabled(false);
                        rlFollow.setFocusable(false);
                        rlCall.setFocusable(true);
                    } else {
                        customAlert("Oops, something went wrong!");
                    }
                } catch (Exception e) {
                    hideRotateDialog();
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
    }

    private void requestCallPermission() {
        requestPermissions(permissions, 100);
    }

    private void requestSmsPermission() {
        requestPermissions(permissions, 101);
    }

    private void requestContactAccessPermission() {
        requestPermissions(permissions, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getCardResponseDataModel.user_phone));
                if (callIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                    startActivity(callIntent);
                }
            } else {
                requestCallPermission();
            }
        } else if (requestCode == 101) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                sendSms();
            } else {
                requestSmsPermission();
            }
        } else if (requestCode == 102) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 100);
            } else {
                requestContactAccessPermission();
            }
        }
    }

    private void sendSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(etNumber.getText().toString(), null, "https://wokk.co.in/card/" + getCardResponseDataModel.user_token, null, null);
            Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupAdapter(ArrayList<GalleryResponseModel> galleryImageList) {
        if (galleryImageList.size() > 0) {
            rvGallery.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            rvGallery.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            CardGalleryAdapter cardGalleryAdapter = new CardGalleryAdapter(this, galleryImageList);
            rvGallery.setAdapter(cardGalleryAdapter);
        } else {
            rvGallery.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    private void setUpYoutubeAdapter(ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList) {
        if (youtubeDetailsModelArrayList.size() > 0) {
            rvYoutube.setVisibility(View.VISIBLE);
            tvEmptyTextYoutube.setVisibility(View.GONE);
            rvYoutube.setLayoutManager(new LinearLayoutManager(getActivity()));
            CardYoutubeAdapter cardYoutubeAdapter = new CardYoutubeAdapter(this, youtubeDetailsModelArrayList);
            rvYoutube.setAdapter(cardYoutubeAdapter);
        } else {
            rvYoutube.setVisibility(View.GONE);
            tvEmptyTextYoutube.setVisibility(View.VISIBLE);
        }
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton = new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
        if (!customAlertWithOneButton.isShowing()) {
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
}
