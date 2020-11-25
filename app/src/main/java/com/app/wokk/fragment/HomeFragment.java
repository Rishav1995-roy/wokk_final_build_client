package com.app.wokk.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.adapter.BannerItemAdapter;
import com.app.wokk.adapter.HomeRecyclerViewAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.BannerItemModel;
import com.app.wokk.model.ServiceClass;
import com.app.wokk.model.ServiceListDataModel;
import com.app.wokk.model.ServiceResponseModelClass;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.app.wokk.utility.AutoViewpager;
import com.app.wokk.utility.GridSpacingItemDecoration;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public View rootView;
    public AutoViewpager viewPager;
    public TabLayout tabDots;
    public TextView tvCountryCode;
    public EditText etNumber;
    public TextView tvEmptyText;
    public RelativeLayout rlShare;
    public RelativeLayout rlSms;
    public RecyclerView recyclerView;
    public static LinearLayout llNumber;
    public MyPreference myPreference;
    private int TIME_INTERVAL = 2000;
    private Long mBackPressed = System.currentTimeMillis();
    public ArrayList<BannerItemModel> bannerList;
    public static ArrayList<ServiceListDataModel> servicesList;
    private BannerItemAdapter bannerItemAdapter;
    private int currentPage = 0;
    private Timer timer;
    private Handler handler;
    private Runnable update;
    private boolean timerIsRunning;
    String[] permissions = {Manifest.permission.SEND_SMS};

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //ContainerActivity.rlMyCardViews.setVisibility(View.GONE);
        init(rootView);
        myPreference = new MyPreference(getActivity());
        if (ContainerActivity.cardDetailsResponseModel != null) {
            ContainerActivity.rlMyCardViews.setVisibility(View.VISIBLE);
            ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
            llNumber.setVisibility(View.VISIBLE);
        } else {
            ContainerActivity.rlMyCardViews.setVisibility(View.GONE);
            ContainerActivity.btnCreateVisitingCard.setVisibility(View.VISIBLE);
            llNumber.setVisibility(View.GONE);
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rootView != null) {
            rootView.setFocusableInTouchMode(true);
            rootView.requestFocus();
            rootView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                            Objects.requireNonNull(getActivity()).onBackPressed();
                            getActivity().finishAffinity();
                        } else {
                            Toast.makeText(getActivity(), "Press agian to exit", Toast.LENGTH_SHORT).show();
                        }
                        mBackPressed = System.currentTimeMillis();
                    }
                    return false;
                }
            });
            /*if (handler==null)
                handler = new Handler();

            if (!timerIsRunning)
                startTimerForAutoScroll();*/

            boolean networkCheck = NetworkCheck.getInstant(getActivity()).isConnectingToInternet();
            if (networkCheck) {
                getServiceList();
            } else {
                customAlert(getResources().getString(R.string.noInternetText));
            }
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
                        setHomeAdapter(servicesList, response.body().url);
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

    private void init(View rootView) {
        viewPager = rootView.findViewById(R.id.viewPager);
        tabDots = rootView.findViewById(R.id.tabDots);
        tvCountryCode = rootView.findViewById(R.id.tvCountryCode);
        etNumber = rootView.findViewById(R.id.etNumber);
        tvEmptyText = rootView.findViewById(R.id.tvEmptyText);
        rlShare = rootView.findViewById(R.id.rlShare);
        rlSms = rootView.findViewById(R.id.rlSms);
        llNumber = rootView.findViewById(R.id.llNumber);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        setDemoData();
        clickListener();
    }

    private void setHomeAdapter(ArrayList<ServiceListDataModel> homelist, String url) {
        if (homelist.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            HomeRecyclerViewAdapter homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(this, homelist, url);
            recyclerView.setAdapter(homeRecyclerViewAdapter);
            myPreference.setLoad(true);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    private void setDemoData() {
        bannerList = new ArrayList<BannerItemModel>();
        bannerList.clear();
        for (int i = 0; i < 3; i++) {
            BannerItemModel bannerItemModel = new BannerItemModel();
            if (i == 0) {
                bannerItemModel.image = 0;
                bannerItemModel.tag = "video";
                bannerItemModel.video = "0-S5a0eXPoc";
            } else if (i == 1) {
                bannerItemModel.image = R.drawable.ambulance;
                bannerItemModel.tag = "image";
                bannerItemModel.video = "";
            } else {
                bannerItemModel.image = R.drawable.salon;
                bannerItemModel.tag = "image";
                bannerItemModel.video = "";
            }
            bannerList.add(bannerItemModel);
        }
        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        bannerItemAdapter = new BannerItemAdapter(getChildFragmentManager(), bannerList);
        viewPager.setAdapter(bannerItemAdapter);
        tabDots.setupWithViewPager(viewPager, true);
    }

    private void clickListener() {
        rlShare.setOnClickListener(this);
        rlSms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rlShare) {
            hideKeyBoardRelativeLayout(rlShare);
            if(etNumber.getText().toString().isEmpty()){
                customAlert("Please enter a mobile number to continue!");
            }else{
                PackageManager packageManager = Objects.requireNonNull(getContext()).getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + etNumber.getText().toString() + "&text=" + URLEncoder.encode("https://wokk.co.in/card/" + ContainerActivity.getCardResponseDataModel.user_token, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        getContext().startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (view.getId() == R.id.rlSms) {
            hideKeyBoardRelativeLayout(rlSms);
            if(etNumber.getText().toString().isEmpty()){
                customAlert("Please enter a mobile number to continue!");
            }else {
                requestPermission();
            }
        }
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
                sendSms();
            } else {
                requestPermission();
            }
        }
    }

    private void sendSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(etNumber.getText().toString(), null, "https://wokk.co.in/card/" + ContainerActivity.getCardResponseDataModel.user_token, null, null);
            Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton = new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
        if (!customAlertWithOneButton.isShowing()) {
            customAlertWithOneButton.show();
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
    public void onPause() {
        super.onPause();
    }
}