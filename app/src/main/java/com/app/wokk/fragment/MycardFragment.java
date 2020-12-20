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
    public RelativeLayout rlYoutube, rlAlert,rlSms,rlWhatsappShare;
    public Button btnFollow,btnShare;
    public TextView tvEmptyText, tvEmptyTextYoutube, tvProfile,tvCardHolderName,tvUserType,tvUserDecsp;
    public ImageView ivInstagram, ivCard, ivTwitter, ivFacebook,ivCardHolderPhone,ivContact,ivCardHolderWhatsapp;
    public GetCardResponseDataModel getCardResponseDataModel;
    public CardDetailsResponseModel cardDetailsResponseModel;
    public ArrayList<GalleryResponseModel> galleryCardList;
    public ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList;
    public ArrayList<AllLayoutsResponseModel> layoutList;
    public int follow_status;
    NestedScrollView scroll;
    public boolean validity_status;
    public String validityDate;
    public String follow_count, viewCount, layoutUrl;
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
                btnFollow.setText("Follow");
                btnFollow.setClickable(true);
                btnFollow.setEnabled(true);
                btnFollow.setFocusable(true);
                btnFollow.setAlpha(1.0f);
            } else if (follow_status == 1) {
                btnFollow.setText("Followed");
                btnFollow.setClickable(false);
                btnFollow.setEnabled(false);
                btnFollow.setFocusable(false);
                btnFollow.setAlpha(0.4f);
            }
        } else {
            btnFollow.setText("Followers: " + follow_count);
            btnFollow.setClickable(false);
            btnFollow.setEnabled(false);
            btnFollow.setFocusable(false);
            btnFollow.setAlpha(0.4f);
            btnFollow.setTextSize(getResources().getDimensionPixelSize(R.dimen._4sdp));
            ivCardHolderWhatsapp.setAlpha(0.4f);
            ivCardHolderWhatsapp.setClickable(false);
            ivCardHolderWhatsapp.setEnabled(false);
            ivCardHolderWhatsapp.setFocusable(false);
            ivCardHolderPhone.setAlpha(0.4f);
            ivCardHolderPhone.setClickable(false);
            ivCardHolderPhone.setEnabled(false);
            ivCardHolderPhone.setFocusable(false);
        }
        if (validity_status) {
            scroll.setVisibility(View.VISIBLE);
            rlAlert.setVisibility(View.GONE);
            if (cardDetailsResponseModel.card_phone_show.equals("0")) {
                ivCardHolderPhone.setVisibility(View.VISIBLE);
                ivCardHolderWhatsapp.setVisibility(View.VISIBLE);
            } else {
                ivCardHolderWhatsapp.setVisibility(View.GONE);
                ivCardHolderPhone.setVisibility(View.GONE);
            }
        }else{
            scroll.setVisibility(View.GONE);
            rlAlert.setVisibility(View.VISIBLE);
        }
    }

    private void init(View rootView) {
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
        btnShare = rootView.findViewById(R.id.btnShare);
        ivCardHolderWhatsapp = rootView.findViewById(R.id.ivCardHolderWhatsapp);
        btnFollow = rootView.findViewById(R.id.btnFollow);
        tvCardHolderName = rootView.findViewById(R.id.tvCardHolderName);
        tvUserType = rootView.findViewById(R.id.tvUserType);
        rlWhatsappShare = rootView.findViewById(R.id.rlWhatsappShare);
        rlSms = rootView.findViewById(R.id.rlSms);
        etNumber = rootView.findViewById(R.id.etNumber);
        rvGallery = rootView.findViewById(R.id.rvGallery);
        clickEvent();
    }

    private void clickEvent() {
        ivCardHolderWhatsapp.setOnClickListener(this);
        rlWhatsappShare.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        ivCardHolderPhone.setOnClickListener(this);
        rlSms.setOnClickListener(this);
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
            case R.id.ivCardHolderWhatsapp:
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
            case R.id.btnFollow:
                doFollow();
                break;
            case R.id.btnShare:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My visiting card");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://wokk.co.in/card/" + getCardResponseDataModel.user_token);
                startActivity(Intent.createChooser(sharingIntent, "Share visiting card via"));
                break;
            case R.id.ivCardHolderPhone:
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
                        btnFollow.setText("Followed");
                        btnFollow.setClickable(false);
                        btnFollow.setFocusable(false);
                        btnFollow.setEnabled(false);
                        btnFollow.setAlpha(0.4f);
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
