package com.app.wokk.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
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
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.wokk.R;
import com.app.wokk.activity.CardEditActivity;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.adapter.ProfileViewpagerAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.utility.SwipeDisableViewpager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static com.app.wokk.activity.ContainerActivity.cardDetailsResponseModel;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public ImageView ivUserCard;
    public ImageView ivFirstStar;
    public ImageView ivSecondStar;
    public ImageView ivThirdStar;
    public ImageView ivFourthStar;
    public ImageView ivFifthStar;
    public ImageView ivInstagram;
    public ImageView ivTwitter,ivcard;
    public ImageView ivFacebook, ivAddress, ivMail, ivPhone;;
    public static Button btnEditCard;
    public static Button btnCreateCard;
    public static RelativeLayout rlCard;
    public SwipeDisableViewpager viewPager;
    public TabLayout tabs;
    public static TextView tvUserType;
    public static TextView tvUsername;
    public TextView tvFollowers;
    public static TextView tvOrganisationName;
    public static TextView tvName;
    public static TextView tvCardAddress;
    public static TextView tvemailAddress;
    public static TextView tvphoneNumber;
    public TextView tvViews;
    public static TextView tvAddress;
    public static LinearLayout llAddress;
    public static LinearLayout llMail;
    public static LinearLayout llPhoneNumber;
    String addressColor,emailColor,phoneColor;
    Typeface organistionTypeface,nameTypeface,addressTypeface,emailtypeface,phoneTypeface;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView=inflater.inflate(R.layout.fragment_profile,container,false);
        myPreference=new MyPreference(getActivity());
        //ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
        //ContainerActivity.rlMyCardViews.setVisibility(View.GONE);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        showRotateDialog();
        ivPhone=rootView.findViewById(R.id.ivPhone);
        ivMail=rootView.findViewById(R.id.ivMail);
        ivAddress=rootView.findViewById(R.id.ivAddress);
        ivcard=rootView.findViewById(R.id.ivcard);
        viewPager=rootView.findViewById(R.id.viewPager);
        ivcard=rootView.findViewById(R.id.ivcard);
        ivFirstStar=rootView.findViewById(R.id.ivFirstStar);
        ivSecondStar=rootView.findViewById(R.id.ivSecondStar);
        ivThirdStar=rootView.findViewById(R.id.ivThirdStar);
        ivFourthStar=rootView.findViewById(R.id.ivFourthStar);
        ivFifthStar=rootView.findViewById(R.id.ivFifthStar);
        ivInstagram=rootView.findViewById(R.id.ivInstagram);
        ivTwitter=rootView.findViewById(R.id.ivTwitter);
        ivFacebook=rootView.findViewById(R.id.ivFacebook);
        btnEditCard=rootView.findViewById(R.id.btnEditCard);
        tabs=rootView.findViewById(R.id.tabs);
        tvUserType=rootView.findViewById(R.id.tvUserType);
        tvUsername=rootView.findViewById(R.id.tvUsername);
        tvFollowers=rootView.findViewById(R.id.tvFollowers);
        tvViews=rootView.findViewById(R.id.tvViews);
        tvAddress=rootView.findViewById(R.id.tvAddress);
        llPhoneNumber=rootView.findViewById(R.id.llPhoneNumber);
        llMail=rootView.findViewById(R.id.llMail);
        llAddress=rootView.findViewById(R.id.llAddress);
        tvphoneNumber=rootView.findViewById(R.id.tvphoneNumber);
        tvemailAddress=rootView.findViewById(R.id.tvemailAddress);
        tvCardAddress=rootView.findViewById(R.id.tvCardAddress);
        tvName=rootView.findViewById(R.id.tvName);
        tvOrganisationName=rootView.findViewById(R.id.tvOrganisationName);
        rlCard=rootView.findViewById(R.id.rlCard);
        btnCreateCard=rootView.findViewById(R.id.btnCreateCard);
        if(cardDetailsResponseModel != null) {
            ContainerActivity.rlViews.setVisibility(View.VISIBLE);
            ContainerActivity.rlCreate.setVisibility(View.GONE);
        }else{
            ContainerActivity.rlViews.setVisibility(View.GONE);
            ContainerActivity.rlCreate.setVisibility(View.VISIBLE);
        }
        clickEvent();
        setUpDataForProfile();
        setUpViewPager(viewPager);
        setTabIcons();
    }

    private class LoadBackground extends AsyncTask<String, Void, Drawable> {

        private String imageUrl , imageName;

        public LoadBackground(String url, String file_name) {
            this.imageUrl = url;
            this.imageName = file_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(String... urls) {

            try {
                InputStream is = (InputStream) this.fetch(this.imageUrl);
                Drawable d = Drawable.createFromStream(is, this.imageName);
                return d;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        private Object fetch(String address) throws MalformedURLException,IOException {
            URL url = new URL(address);
            Object content = url.getContent();
            return content;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            super.onPostExecute(result);
            //rlCard.setBackgroundDrawable(result);
        }
    }

    private void setUpDataForProfile() {
        if(cardDetailsResponseModel != null){
            rlCard.setVisibility(View.VISIBLE);
            ivPhone.setVisibility(View.VISIBLE);
            ivAddress.setVisibility(View.VISIBLE);
            ivMail.setVisibility(View.VISIBLE);
            btnEditCard.setVisibility(View.VISIBLE);
            btnCreateCard.setVisibility(View.GONE);
            tvOrganisationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_org_fontsize_mob));
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_name_fontsize_mob));
            tvphoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_phone_fontsize_mob));
            tvCardAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_address_fontsize_mob));
            tvemailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(cardDetailsResponseModel.card_email_fontsize_mob));
            if(ContainerActivity.getCardResponseDataModel.user_fname != null && ContainerActivity.getCardResponseDataModel.user_lname != null)
                tvName.setText(ContainerActivity.getCardResponseDataModel.user_fname+" "+ContainerActivity.getCardResponseDataModel.user_lname);
            /*GradientDrawable gd = new GradientDrawable();
            //gd.setColor(Color.RED);
            //gd.setCornerRadius(10);
            gd.setStroke(25, Color.parseColor(cardDetailsResponseModel.card_border_color));*/
            ivcard.setBackgroundColor(Color.parseColor(cardDetailsResponseModel.card_border_color));
            Glide.with(this).load(ContainerActivity.layoutUrl + cardDetailsResponseModel.layout_image).into(ivcard);
            if(ContainerActivity.getCardResponseDataModel.user_address != null && ContainerActivity.getCardResponseDataModel.user_pin != null && !ContainerActivity.getCardResponseDataModel.user_address.equals("null") && !ContainerActivity.getCardResponseDataModel.user_pin.equals("null"))
                tvCardAddress.setText(ContainerActivity.getCardResponseDataModel.user_address+" - "+ ContainerActivity.getCardResponseDataModel.user_pin);
            if(ContainerActivity.getCardResponseDataModel.user_phone != null)
                tvphoneNumber.setText(ContainerActivity.getCardResponseDataModel.user_phone);
            if(ContainerActivity.getCardResponseDataModel.user_organization_name != null)
                tvOrganisationName.setText(ContainerActivity.getCardResponseDataModel.user_organization_name);
            if(ContainerActivity.getCardResponseDataModel.user_email != null)
                tvemailAddress.setText(ContainerActivity.getCardResponseDataModel.user_email);
            RelativeLayout.LayoutParams tvNameParams = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
            if(cardDetailsResponseModel.card_name_top_mob != null)
                tvNameParams.topMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_name_top_mob));
            else
                tvNameParams.topMargin=0;
            if(cardDetailsResponseModel.card_name_left_mob != null)
                tvNameParams.leftMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_name_left_mob));
            else
                tvNameParams.leftMargin=0;
            tvName.setLayoutParams(tvNameParams);
            RelativeLayout.LayoutParams tvOrganisationParams = (RelativeLayout.LayoutParams) tvOrganisationName.getLayoutParams();
            if(cardDetailsResponseModel.card_org_top_mob != null)
                tvOrganisationParams.topMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_org_top_mob));
            else
                tvOrganisationParams.topMargin=0;
            if(cardDetailsResponseModel.card_org_left_mob != null)
                tvOrganisationParams.leftMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_org_left_mob));
            else
                tvOrganisationParams.leftMargin=0;
            tvOrganisationName.setLayoutParams(tvOrganisationParams);
            RelativeLayout.LayoutParams addressParams = (RelativeLayout.LayoutParams) llAddress.getLayoutParams();
            if(cardDetailsResponseModel.card_address_top_mob != null)
                addressParams.topMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_address_top_mob));
            else
                addressParams.topMargin=0;
            if(cardDetailsResponseModel.card_address_left_mob != null)
                addressParams.leftMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_address_left_mob));
            else
                addressParams.leftMargin=0;
            llAddress.setLayoutParams(addressParams);
            RelativeLayout.LayoutParams phoneParams = (RelativeLayout.LayoutParams) llPhoneNumber.getLayoutParams();
            if(cardDetailsResponseModel.card_phone_top_mob != null)
                phoneParams.topMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_phone_top_mob));
            else
                phoneParams.topMargin=0;
            if(cardDetailsResponseModel.card_phone_left_mob != null)
                phoneParams.leftMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_phone_left_mob));
            else
                phoneParams.leftMargin=0;
            llPhoneNumber.setLayoutParams(phoneParams);
            RelativeLayout.LayoutParams mailParams = (RelativeLayout.LayoutParams) llMail.getLayoutParams();
            if(cardDetailsResponseModel.card_email_top_mob != null)
                mailParams.topMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_email_top_mob));
            else
                mailParams.topMargin=0;
            if(cardDetailsResponseModel.card_email_left_mob != null)
                mailParams.leftMargin=Integer.parseInt(String.valueOf(cardDetailsResponseModel.card_email_left_mob));
            else
                mailParams.leftMargin=0;
            llMail.setLayoutParams(mailParams);
            if(cardDetailsResponseModel.card_org_color.contains("0") && cardDetailsResponseModel.card_org_color.length() == 4){
                tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color+"000"));
            }else if(cardDetailsResponseModel.card_org_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_org_color.length() == 4){
                tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color.toLowerCase()+"fff"));
            }else{
                tvOrganisationName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_org_color));
            }
            if(cardDetailsResponseModel.card_name_color.contains("0") && cardDetailsResponseModel.card_name_color.length() == 4){
                tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color+"000"));
            }else if(cardDetailsResponseModel.card_name_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_name_color.length() == 4){
                tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color.toLowerCase()+"fff"));
            }else{
                tvName.setTextColor(Color.parseColor(cardDetailsResponseModel.card_name_color));
            }
            if(cardDetailsResponseModel.card_address_color.contains("0") && cardDetailsResponseModel.card_address_color.length() == 4){
                tvCardAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color+"000"));
                addressColor=cardDetailsResponseModel.card_address_color+"000";
            }else if(cardDetailsResponseModel.card_address_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_address_color.length() == 4){
                tvCardAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color.toLowerCase()+"fff"));
                addressColor=cardDetailsResponseModel.card_address_color+"fff";
            }else{
                tvCardAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_address_color));
                addressColor=cardDetailsResponseModel.card_address_color;
            }
            ImageViewCompat.setImageTintMode(ivAddress, PorterDuff.Mode.SRC_ATOP);
            ImageViewCompat.setImageTintList(ivAddress, ColorStateList.valueOf(Color.parseColor(addressColor)));
            if(cardDetailsResponseModel.card_email_color.contains("0") && cardDetailsResponseModel.card_email_color.length() == 4){
                tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color+"000"));
                emailColor=cardDetailsResponseModel.card_email_color+"000";
            }else if(cardDetailsResponseModel.card_email_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_email_color.length() == 4){
                tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color.toLowerCase()+"fff"));
                emailColor=cardDetailsResponseModel.card_email_color+"fff";
            }else{
                tvemailAddress.setTextColor(Color.parseColor(cardDetailsResponseModel.card_email_color));
                emailColor=cardDetailsResponseModel.card_email_color;
            }
            ImageViewCompat.setImageTintMode(ivMail, PorterDuff.Mode.SRC_ATOP);
            ImageViewCompat.setImageTintList(ivMail, ColorStateList.valueOf(Color.parseColor(emailColor)));
            if(cardDetailsResponseModel.card_phone_color.contains("0") && cardDetailsResponseModel.card_phone_color.length() == 4){
                tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color+"000"));
                phoneColor=cardDetailsResponseModel.card_phone_color+"000";
            }else if(cardDetailsResponseModel.card_phone_color.toLowerCase().contains("f") && cardDetailsResponseModel.card_phone_color.length() == 4){
                tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color.toLowerCase()+"fff"));
                phoneColor=cardDetailsResponseModel.card_phone_color+"fff";
            }else{
                tvphoneNumber.setTextColor(Color.parseColor(cardDetailsResponseModel.card_phone_color));
                phoneColor=cardDetailsResponseModel.card_phone_color;
            }
            ImageViewCompat.setImageTintMode(ivPhone, PorterDuff.Mode.SRC_ATOP);
            ImageViewCompat.setImageTintList(ivPhone, ColorStateList.valueOf(Color.parseColor(phoneColor)));
            if(cardDetailsResponseModel.card_org_show.equals("0")){
                tvOrganisationName.setVisibility(View.GONE);
            }else{
                tvOrganisationName.setVisibility(View.VISIBLE);
            }
            if(cardDetailsResponseModel.card_name_show.equals("0")){
                tvName.setVisibility(View.GONE);
            }else{
                tvName.setVisibility(View.VISIBLE);
            }
            if(cardDetailsResponseModel.card_address_show.equals("0")){
                llAddress.setVisibility(View.GONE);
            }else{
                llAddress.setVisibility(View.VISIBLE);
            }
            if(cardDetailsResponseModel.card_email_show.equals("0")){
                llMail.setVisibility(View.GONE);
            }else{
                llMail.setVisibility(View.VISIBLE);
            }
            if(cardDetailsResponseModel.card_phone_show.equals("0")){
                llPhoneNumber.setVisibility(View.GONE);
            }else{
                llPhoneNumber.setVisibility(View.VISIBLE);
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
                //organisationFont=ContainerActivity.cardDetailsResponseModel.card_org_font;
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
                //nameFont=ContainerActivity.cardDetailsResponseModel.card_name_font;
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
                //emailFont=ContainerActivity.cardDetailsResponseModel.card_email_font;
            }
            if(cardDetailsResponseModel.card_address_font != null){
                if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("courier new, monospace")){
                    addressTypeface=getResources().getFont(R.font.courierprime_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("inconsolata")){
                    addressTypeface=getResources().getFont(R.font.inconsolata_variablefont_wdth_wght);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("recursive")){
                    addressTypeface=getResources().getFont(R.font.recursive_variablefont_casl_crsv_mono_slnt_wght);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("cedarville cursive")){
                    addressTypeface=getResources().getFont(R.font.courierprime_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("noto sans")){
                    addressTypeface=getResources().getFont(R.font.notosans_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("poppins")){
                    addressTypeface=getResources().getFont(R.font.poppins_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("open sans")){
                    addressTypeface=getResources().getFont(R.font.opensans_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("roboto")){
                    addressTypeface=getResources().getFont(R.font.roboto_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("montserrat")){
                    addressTypeface=getResources().getFont(R.font.montserrat_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("lato")){
                    addressTypeface=getResources().getFont(R.font.lato_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("source sans pro")){
                    addressTypeface=getResources().getFont(R.font.sourcesanspro_regular);
                    tvCardAddress.setTypeface(addressTypeface);
                }else if(cardDetailsResponseModel.card_address_font.toLowerCase().equals("raleway, sans-serif")){
                    addressTypeface=getResources().getFont(R.font.raleway_variablefont_wght);
                    tvCardAddress.setTypeface(addressTypeface);
                }
                //addressFont=ContainerActivity.cardDetailsResponseModel.card_address_font;
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
                //phoneFont=ContainerActivity.cardDetailsResponseModel.card_phone_font;
            }
            tvAddress.setText(ContainerActivity.getCardResponseDataModel.user_address+" - "+ ContainerActivity.getCardResponseDataModel.user_pin);
            tvUsername.setText(ContainerActivity.getCardResponseDataModel.user_fname+" "+ContainerActivity.getCardResponseDataModel.user_lname);
        }else{
            ivPhone.setVisibility(View.GONE);
            ivAddress.setVisibility(View.GONE);
            ivMail.setVisibility(View.GONE);
            tvAddress.setText("");
            tvUsername.setText(ContainerActivity.getCardResponseDataModel.user_phone);
            tvUserType.setText("");
            rlCard.setVisibility(View.GONE);
            btnEditCard.setVisibility(View.GONE);
            btnCreateCard.setVisibility(View.VISIBLE);
        }
        tvViews.setText(ContainerActivity.viewCount);
        tvFollowers.setText("0");
        for(int i=0;i<HomeFragment.servicesList.size();i++){
            if(HomeFragment.servicesList.get(i).service_id.equals(ContainerActivity.getCardResponseDataModel.user_service_id)){
                tvUserType.setText(HomeFragment.servicesList.get(i).service_name);
                break;
            }
        }
        hideRotateDialog();
    }

    private void setTabIcons() {
        Objects.requireNonNull(tabs.getTabAt(0)).setIcon(R.drawable.tab_about_icon);
        Objects.requireNonNull(tabs.getTabAt(1)).setIcon(R.drawable.tab_gallery_icon);
        Objects.requireNonNull(tabs.getTabAt(2)).setIcon(R.drawable.tab_gallery_icon);
    }

    private void setUpViewPager(SwipeDisableViewpager viewPager) {
        ProfileViewpagerAdapter profileViewpagerAdapter=new ProfileViewpagerAdapter(getChildFragmentManager());
        profileViewpagerAdapter.addFragment(UserDetailsFragment.newInstance(),"About");
        profileViewpagerAdapter.addFragment(UserGalleryFragment.newInstance(),"Gallery");
        profileViewpagerAdapter.addFragment(UserYoutubeFragment.newInstance(),"Youtube");
        viewPager.setAdapter(profileViewpagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        tabs.setupWithViewPager(viewPager);
    }

    private void clickEvent() {
        btnEditCard.setOnClickListener(this);
        ivInstagram.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        ivFacebook.setOnClickListener(this);
        btnCreateCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreateCard:
                Fragment cardCreateFragment = CreateCardFragment.newInstance();
                FragmentTransaction cardCreateTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                cardCreateTransaction.replace(R.id.frameContainerChild, cardCreateFragment);
                cardCreateTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
                cardCreateTransaction.addToBackStack(null);
                cardCreateTransaction.commit();
                break;
            case R.id.btnEditCard:
                Intent intent = new Intent(getActivity(), CardEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                (getActivity()).startActivity(intent);
                break;
            case R.id.ivInstagram:
                customAlert("This section is in under development.");
                break;
            case R.id.ivTwitter:
                customAlert("This section is in under development.");
                break;
            case R.id.ivFacebook:
                customAlert("This section is in under development.");
                break;
        }
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
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
}
