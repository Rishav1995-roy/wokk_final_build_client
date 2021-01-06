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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    public ImageView ivTwitter,ivcard,ivFacebook,ivFirstStar,ivSecondStar,ivThirdStar,ivFourthStar,ivFifthStar,ivInstagram;
    public static Button btnEditCard,btnCreateCard;
    public SwipeDisableViewpager viewPager;
    public TabLayout tabs;
    public static TextView tvUserType,tvUsername;
    public TextView tvFollowers;
    public TextView tvViews;
    public static TextView tvAddress;

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
        ivcard=rootView.findViewById(R.id.ivCard);
        viewPager=rootView.findViewById(R.id.viewPager);
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
        if(ContainerActivity.getCardResponseDataModel != null && cardDetailsResponseModel != null){
            ivcard.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(cardDetailsResponseModel.card_image_url).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(ivcard);
            tvAddress.setText(ContainerActivity.getCardResponseDataModel.user_address+" - "+ContainerActivity.getCardResponseDataModel.user_pin);
            tvUsername.setText(ContainerActivity.getCardResponseDataModel.user_fname);
            btnEditCard.setVisibility(View.VISIBLE);
            btnCreateCard.setVisibility(View.GONE);
        }else{
            ivcard.setVisibility(View.GONE);
            tvAddress.setText("");
            tvUsername.setText(ContainerActivity.getCardResponseDataModel.user_phone);
            tvUserType.setText("");
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
    }

    private void setTabIcons() {
        Objects.requireNonNull(tabs.getTabAt(0)).setIcon(R.drawable.tab_about_icon);
        Objects.requireNonNull(tabs.getTabAt(1)).setIcon(R.drawable.tab_gallery_icon);
        Objects.requireNonNull(tabs.getTabAt(2)).setIcon(R.drawable.tab_youtube_icon);
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
