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
import android.widget.Button;
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
import com.app.wokk.model.ServiceClass;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.model.ServiceResponseModelClass;
import com.app.wokk.model.YoutubeDetailsModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import static com.app.wokk.activity.ContainerActivity.cardDetailsResponseModel;
import static com.app.wokk.activity.ContainerActivity.tvViews;

public class MycardFragment extends BaseFragment implements View.OnClickListener {

    public static MycardFragment newInstance() {
        return new MycardFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public RecyclerView rvGallery, rvYoutube;
    public EditText etNumber;
    public LinearLayout llabout,llgallery,llAbout,llname,llOrg,llPhoneNumber,llPin,llAddress,llEmail;
    public RelativeLayout rlYoutube, rlAlert,rlSms,rlWhatsappShare,rlFollow,rlWapp,rlCall,rlshare,rlWokkYoutubeLink,rlGallery;
    public TextView tvEmptyText, tvEmptyTextYoutube, tvProfile,tvCardHolderName,tvUserType,tvUserDecsp,tvFollow,tvEmail,tvPin,tvAddress,tvPh,tvOrg,tvName,tvService,tvAbout,tvGallery;
    public ImageView ivInstagram, ivCard, ivTwitter, ivFacebook,ivCardHolderPhone,ivContact,ivCardHolderWhatsapp,ivFollow,ivAbout,ivGallery;
    public GetCardResponseDataModel getCardResponseDataModel;
    public CardDetailsResponseModel cardDetailsResponseModel;
    public ArrayList<GalleryResponseModel> galleryCardList;
    public ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList;
    public static ArrayList<ServiceListDataModel> servicesList;
    public ArrayList<AllLayoutsResponseModel> layoutList;
    public int follow_status;
    NestedScrollView scroll;
    public boolean validity_status;
    public String validityDate;
    public String follow_count, viewCount, layoutUrl;
    String[] permissions = { Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS};

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
            getServiceList();
        } else {
            customAlert(getResources().getString(R.string.noInternetText));
        }
    }

    private void getServiceList() {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel = new ApiCredentialModel();
        apiCredentialModel.apiuser = Constant.apiuser;
        apiCredentialModel.apipass = Constant.apipass;
        ServiceClass serviceClass = new ServiceClass();
        serviceClass.apiCredentialModel = apiCredentialModel;
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(serviceClass);
        Call<ServiceResponseModelClass> getService = RestManager.getInstance().getService().get_service(jsonElement);
        getService.enqueue(new Callback<ServiceResponseModelClass>() {
            @Override
            public void onResponse(@NotNull Call<ServiceResponseModelClass> call, @NotNull Response<ServiceResponseModelClass> response) {
                hideRotateDialog();
                try {
                    assert response.body() != null;
                    int code = response.body().code;
                    if (code == 1) {
                        servicesList = new ArrayList<>();
                        servicesList.clear();
                        servicesList = response.body().data;
                        getUserdetails();
                    } else if (code == 9) {
                        customAlert("An authentication error occured!");
                    } else {
                        customAlert("Oops, something went wrong!");
                    }
                } catch (Exception e) {
                    customAlert("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ServiceResponseModelClass> call, @NotNull Throwable t) {
                hideRotateDialog();
                customAlert("Internal server error. Please try after few minutes.");
            }
        });
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
            ivFollow.setVisibility(View.VISIBLE);
            if (follow_status == 0) {
                tvFollow.setText("Follow");
                rlFollow.setClickable(true);
                rlFollow.setEnabled(true);
                rlFollow.setFocusable(true);
                rlFollow.setAlpha(1.0f);
            } else if (follow_status == 1) {
                tvFollow.setText("Followed");
                tvFollow.setTextSize(getResources().getDimensionPixelSize(R.dimen._3sdp));
                rlFollow.setClickable(false);
                rlFollow.setEnabled(false);
                rlFollow.setFocusable(false);
                rlFollow.setAlpha(0.4f);
            }
        } else {
            ivFollow.setVisibility(View.GONE);
            tvFollow.setText("Followers: " + follow_count);
            rlFollow.setClickable(false);
            rlFollow.setEnabled(false);
            rlFollow.setFocusable(false);
            rlFollow.setAlpha(0.4f);
            tvFollow.setTextSize(getResources().getDimensionPixelSize(R.dimen._3sdp));
            rlCall.setAlpha(0.4f);
            rlCall.setClickable(false);
            rlCall.setEnabled(false);
            rlCall.setFocusable(false);
            rlWapp.setAlpha(0.4f);
            rlWapp.setClickable(false);
            rlWapp.setEnabled(false);
            rlWapp.setFocusable(false);
        }
        if (validity_status) {
            scroll.setVisibility(View.VISIBLE);
            rlAlert.setVisibility(View.GONE);
            Glide.with(getActivity()).load(cardDetailsResponseModel.card_image_url).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(ivCard);
            tvName.setText(getCardResponseDataModel.user_fname);
            tvOrg.setText(getCardResponseDataModel.user_organization_name);
            tvAddress.setText(getCardResponseDataModel.user_address);
            tvPh.setText(getCardResponseDataModel.user_phone);
            tvPin.setText(getCardResponseDataModel.user_pin);
            tvEmail.setText(getCardResponseDataModel.user_email);
            for(int i=0;i<servicesList.size();i++){
                if(servicesList.get(i).service_id.equals(getCardResponseDataModel.user_service_id)){
                    tvService.setText(servicesList.get(i).service_name);
                }
            }
            if (cardDetailsResponseModel.card_name_show.equals("0")) {
                tvName.setVisibility(View.GONE);
            } else {
                tvName.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_org_show.equals("0")) {
                tvOrg.setVisibility(View.GONE);
            } else {
                tvOrg.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_phone_show.equals("0")) {
                tvPh.setVisibility(View.GONE);
            } else {
                tvPh.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_email_show.equals("0")) {
                tvEmail.setVisibility(View.GONE);
            } else {
                tvEmail.setVisibility(View.VISIBLE);
            }
            if (cardDetailsResponseModel.card_address_show.equals("0")) {
                tvPin.setVisibility(View.GONE);
                tvAddress.setVisibility(View.GONE);
            } else {
                tvPin.setVisibility(View.VISIBLE);
                tvAddress.setVisibility(View.VISIBLE);
            }
            //tvUserDecsp.setText(getCardResponseDataModel.user_organization_desc);
            if (cardDetailsResponseModel.card_phone_show.equals("1")) {
                rlCall.setAlpha(1.0f);
                rlCall.setClickable(true);
                rlCall.setEnabled(true);
                rlCall.setFocusable(true);
                rlWapp.setAlpha(1.0f);
                rlWapp.setClickable(true);
                rlWapp.setEnabled(true);
                rlWapp.setFocusable(true);
            } else {
                rlCall.setAlpha(0.4f);
                rlCall.setClickable(false);
                rlCall.setEnabled(false);
                rlCall.setFocusable(false);
                rlWapp.setAlpha(0.4f);
                rlWapp.setClickable(false);
                rlWapp.setEnabled(false);
                rlWapp.setFocusable(false);
            }
        }else{
            scroll.setVisibility(View.GONE);
            rlAlert.setVisibility(View.VISIBLE);
        }
    }

    private void init(View rootView) {
        tvAddress = rootView.findViewById(R.id.tvAddress);
        rlGallery = rootView.findViewById(R.id.rlGallery);
        ivGallery = rootView.findViewById(R.id.ivGallery);
        tvGallery = rootView.findViewById(R.id.tvGallery);
        llabout = rootView.findViewById(R.id.llabout);
        llgallery = rootView.findViewById(R.id.llgallery);
        llAbout = rootView.findViewById(R.id.llAbout);
        ivAbout = rootView.findViewById(R.id.ivAbout);
        llname = rootView.findViewById(R.id.llname);
        llOrg = rootView.findViewById(R.id.llOrg);
        llPhoneNumber = rootView.findViewById(R.id.llPhoneNumber);
        llEmail = rootView.findViewById(R.id.llEmail);
        llAddress = rootView.findViewById(R.id.llAddress);
        tvEmail = rootView.findViewById(R.id.tvEmail);
        tvPin = rootView.findViewById(R.id.tvPin);
        tvPh = rootView.findViewById(R.id.tvPh);
        tvOrg = rootView.findViewById(R.id.tvOrg);
        tvService = rootView.findViewById(R.id.tvService);
        tvName = rootView.findViewById(R.id.tvName);
        tvAbout = rootView.findViewById(R.id.tvAbout);
        tvGallery = rootView.findViewById(R.id.tvGallery);
        tvFollow = rootView.findViewById(R.id.tvFollow);
        ivCard = rootView.findViewById(R.id.ivCard);
        tvUserDecsp = rootView.findViewById(R.id.tvUserDecsp);
        rlAlert = rootView.findViewById(R.id.rlAlert);
        scroll = rootView.findViewById(R.id.scroll);
        tvProfile = rootView.findViewById(R.id.tvProfile);
        tvEmptyTextYoutube = rootView.findViewById(R.id.tvEmptyTextYoutube);
        rvYoutube = rootView.findViewById(R.id.rvYoutube);
        rlYoutube = rootView.findViewById(R.id.rlYoutube);
        ivFacebook = rootView.findViewById(R.id.ivFirstStar);
        ivTwitter = rootView.findViewById(R.id.ivTwitter);
        ivInstagram = rootView.findViewById(R.id.ivInstagram);
        ivContact = rootView.findViewById(R.id.ivContact);
        tvEmptyText = rootView.findViewById(R.id.tvEmptyText);
        ivCardHolderPhone = rootView.findViewById(R.id.ivCardHolderPhone);
        ivCardHolderWhatsapp = rootView.findViewById(R.id.ivCardHolderWhatsapp);
        tvCardHolderName = rootView.findViewById(R.id.tvCardHolderName);
        tvUserType = rootView.findViewById(R.id.tvUserType);
        rlWhatsappShare = rootView.findViewById(R.id.rlWhatsappShare);
        rlSms = rootView.findViewById(R.id.rlSms);
        etNumber = rootView.findViewById(R.id.etNumber);
        rvGallery = rootView.findViewById(R.id.rvGallery);
        rlWokkYoutubeLink = rootView.findViewById(R.id.rlWokkYoutubeLink);
        rlCall = rootView.findViewById(R.id.rlCall);
        rlshare = rootView.findViewById(R.id.rlshare);
        rlFollow = rootView.findViewById(R.id.rlFollow);
        rlWapp = rootView.findViewById(R.id.rlWapp);
        ivFollow = rootView.findViewById(R.id.ivFollow);
        rlGallery.setVisibility(View.VISIBLE);
        clickEvent();
    }

    private void clickEvent() {
        llabout.setOnClickListener(this);
        llgallery.setOnClickListener(this);
        ivCardHolderWhatsapp.setOnClickListener(this);
        rlWhatsappShare.setOnClickListener(this);
        rlFollow.setOnClickListener(this);
        rlshare.setOnClickListener(this);
        rlCall.setOnClickListener(this);
        rlWapp.setOnClickListener(this);
        rlWokkYoutubeLink.setOnClickListener(this);
        ivCardHolderPhone.setOnClickListener(this);
        rlSms.setOnClickListener(this);
        tvProfile.setOnClickListener(this);
        ivContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llabout:
                llAbout.setVisibility(View.VISIBLE);
                rlGallery.setVisibility(View.GONE);
                ivAbout.setImageResource(R.drawable.mycard_about_active);
                ivGallery.setImageResource(R.drawable.mycard_gallery);
                tvGallery.setTextColor(getResources().getColor(R.color.black));
                tvAbout.setTextColor(getResources().getColor(R.color.orange));
                break;
            case R.id.llgallery:
                llAbout.setVisibility(View.GONE);
                rlGallery.setVisibility(View.VISIBLE);
                ivAbout.setImageResource(R.drawable.mycard_about);
                ivGallery.setImageResource(R.drawable.mycard_gallery_active);
                tvGallery.setTextColor(getResources().getColor(R.color.orange));
                tvAbout.setTextColor(getResources().getColor(R.color.black));
                break;
            case  R.id.rlWokkYoutubeLink:
                customAlert("This is is in under development!");
                break;
            case R.id.tvProfile:
                Fragment profileFragment = ProfileFragment.newInstance();
                FragmentTransaction profileTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                profileTransaction.replace(R.id.frameContainerChild, profileFragment);
                profileTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                profileTransaction.addToBackStack(null);
                profileTransaction.commit();
                break;
            case R.id.rlWapp:
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
                doFollow();
                break;
            case R.id.rlshare:
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
                    sendSms();
                }
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
                    String num="";
                    if(number.contains("+91")){
                       num= number.replace("+91","");
                    }
                    //String num=number.replaceAll("/+91","");
                    //Do something with number
                    etNumber.setText(num);
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
                        tvFollow.setText("Followed");
                        tvFollow.setTextSize(getResources().getDimensionPixelOffset(R.dimen._3sdp));
                        rlFollow.setClickable(false);
                        rlFollow.setFocusable(false);
                        rlFollow.setEnabled(false);
                        rlFollow.setAlpha(0.4f);
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
        }  else if (requestCode == 102) {
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
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address",etNumber.getText().toString());
            smsIntent.putExtra("sms_body","https://wokk.co.in/card/" + getCardResponseDataModel.user_token);
            smsIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(smsIntent);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupAdapter(ArrayList<GalleryResponseModel> galleryImageList) {
        if (galleryImageList.size() > 0) {
            rvGallery.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            rvGallery.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
