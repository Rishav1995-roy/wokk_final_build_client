package com.app.wokk.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.wokk.R;
import com.app.wokk.combine.BaseClass;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.customAlert.CustomAlertWithTwoButton;
import com.app.wokk.fragment.CreateCardFragment;
import com.app.wokk.fragment.HomeFragment;
import com.app.wokk.fragment.MycardFragment;
import com.app.wokk.fragment.ProfileFragment;
import com.app.wokk.fragment.ServicesFragment;
import com.app.wokk.model.AllLayoutsResponseModel;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.CardDetailsResponseModel;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseDataModel;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.YoutubeDetailsModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.wokk.fragment.HomeFragment.llNumber;

public class ContainerActivity extends BaseClass implements View.OnClickListener {

    public static ImageView ivMenu;
    public static Button btnCreateVisitingCard;
    public static RelativeLayout rlMyCardViews,rlCreate,rlViews;
    public static TextView tvViews,tvUser,tvViewCount;
    public FrameLayout frameContainerParent,frameContainerChild;
    public DrawerLayout drawerLayout;
    public View drawerContainer,drawerContent;
    public ImageView ivUser;
    public LinearLayout llProfile,llAbout,llServices,llHome;
    public static LinearLayout llMyCard,llCreateCard,llEditCard;
    public RelativeLayout rlLogout;
    public TextView tvVersionName;
    public ActionBarDrawerToggle toggle;
    public MyPreference myPreference;
    public static GetCardResponseDataModel getCardResponseDataModel;
    public static CardDetailsResponseModel cardDetailsResponseModel;
    public static String layoutUrl,viewCount,follow_count;
    public static boolean validity_status;
    public static String validityDate;
    public static ArrayList<AllLayoutsResponseModel> layoutList;
    public static ArrayList<GalleryResponseModel> galleryList;
    public static ArrayList<YoutubeDetailsModel> youtubeDetailsModelArrayList;
    public static int follow_status;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_container);
        myPreference=new MyPreference(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean networkCheck = NetworkCheck.getInstant(getApplicationContext()).isConnectingToInternet();
        if (networkCheck) {
            getUserdetails();
        }else{
            customAlert(getResources().getString(R.string.noInternetText));
        }
        if (myPreference.getFromCardEdit()){
            Fragment profileFragment = ProfileFragment.newInstance();
            FragmentTransaction profileTransaction = getSupportFragmentManager().beginTransaction();
            profileTransaction.replace(R.id.frameContainerChild, profileFragment);
            profileTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            profileTransaction.addToBackStack(null);
            getSupportFragmentManager().executePendingTransactions();
            profileTransaction.commit();
        }
    }

    private void getUserdetails() {
        showRotateDialog();
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
                        galleryList=new ArrayList<>();
                        layoutList=new ArrayList<>();
                        youtubeDetailsModelArrayList=new ArrayList<>();
                        galleryList.clear();
                        layoutList.clear();
                        youtubeDetailsModelArrayList.clear();
                        layoutUrl=response.body().layout_url;
                        viewCount=response.body().view_count;
                        galleryList=response.body().gallery_details;
                        cardDetailsResponseModel=response.body().card_details;
                        youtubeDetailsModelArrayList=response.body().youtube_details;
                        layoutList=response.body().all_layouts;
                        getCardResponseDataModel=response.body().user_details;
                        validity_status=response.body().validity_status;
                        follow_status=response.body().follow_status;
                        follow_count=response.body().no_of_followers;
                        if(cardDetailsResponseModel != null) {
                            rlViews.setVisibility(View.VISIBLE);
                            if(viewCount != null){
                                if(Integer.parseInt(viewCount)>1){
                                    tvViews.setText("Views");
                                }else{
                                    tvViews.setText("View");
                                }
                                tvViewCount.setText(viewCount);
                            }else{
                                tvViews.setText("View");
                                tvViewCount.setText("0");
                            }
                            rlCreate.setVisibility(View.GONE);
                            llNumber.setVisibility(View.VISIBLE);
                        }else{
                            rlViews.setVisibility(View.GONE);
                            rlCreate.setVisibility(View.VISIBLE);
                            llNumber.setVisibility(View.GONE);
                        }
                        validityDate=getCardResponseDataModel.user_card_valid_until;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        ivMenu=findViewById(R.id.ivMenu);
        rlCreate=findViewById(R.id.rlCreate);
        tvViewCount=findViewById(R.id.tvViewCount);
        tvViews=findViewById(R.id.tvViews);
        rlViews=findViewById(R.id.rlViews);
        tvVersionName=findViewById(R.id.tvVersionName);
        rlLogout=findViewById(R.id.rlLogout);
        llHome=findViewById(R.id.llHome);
        llServices=findViewById(R.id.llServices);
        llAbout=findViewById(R.id.llAbout);
        llProfile=findViewById(R.id.llProfile);
        llMyCard=findViewById(R.id.llMyCard);
        llEditCard=findViewById(R.id.llEditCard);
        llCreateCard=findViewById(R.id.llCreateCard);
        tvUser=findViewById(R.id.tvUser);
        ivUser=findViewById(R.id.ivUser);
        drawerContent=findViewById(R.id.drawerContent);
        drawerContainer=findViewById(R.id.drawerContainer);
        drawerLayout=findViewById(R.id.drawerLayout);
        frameContainerChild=findViewById(R.id.frameContainerChild);
        frameContainerParent=findViewById(R.id.frameContainerParent);
        drawerLayout.setScrimColor(this.getColor(android.R.color.transparent));
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                frameContainerParent.setTranslationX(drawerContainer.getWidth() * slideOffset);
                boolean networkCheck = NetworkCheck.getInstant(getApplicationContext()).isConnectingToInternet();
                if (networkCheck) {
                    setDrawer();
                } else {
                    customAlert(getResources().getString(R.string.noInternetText));
                }
            }
        };
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(toggle);
        Fragment newFragment = HomeFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainerChild, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        clickEvent();
    }

    private void clickEvent() {
        ivMenu.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        llServices.setOnClickListener(this);
        llHome.setOnClickListener(this);
        rlLogout.setOnClickListener(this);
        rlCreate.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llMyCard.setOnClickListener(this);
        llCreateCard.setOnClickListener(this);
        llEditCard.setOnClickListener(this);
    }

    private void setDrawer() {
        tvVersionName.setText(R.string.versionName);
        if(getCardResponseDataModel != null){
            if(getCardResponseDataModel.user_fname != null){
                if(getCardResponseDataModel.user_lname != null){
                    tvUser.setText("Hi, "+getCardResponseDataModel.user_fname+" "+getCardResponseDataModel.user_lname);
                }else{
                    tvUser.setText("Hi, "+getCardResponseDataModel.user_fname);
                }
            }else{
                if(!getCardResponseDataModel.user_phone.equals(""))
                    tvUser.setText((getCardResponseDataModel.user_phone));
            }
            if(cardDetailsResponseModel == null){
                llEditCard.setVisibility(View.GONE);
                llCreateCard.setVisibility(View.VISIBLE);
                llMyCard.setVisibility(View.GONE);
            }else{
                llEditCard.setVisibility(View.VISIBLE);
                llCreateCard.setVisibility(View.GONE);
                llMyCard.setVisibility(View.VISIBLE);
            }
        }
    }

    private void customAlert(String s) {
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llEditCard:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, CardEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
                break;
            case R.id.llCreateCard:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment createCardFragment = CreateCardFragment.newInstance();
                FragmentTransaction createCardTransaction = getSupportFragmentManager().beginTransaction();
                createCardTransaction.replace(R.id.frameContainerChild, createCardFragment);
                createCardTransaction.addToBackStack(null);
                createCardTransaction.commit();
                break;
            case R.id.llMyCard:
                drawerLayout.closeDrawer(GravityCompat.START);
                myPreference.setServiceUserID(myPreference.getUserID());
                Fragment myCardFragment = MycardFragment.newInstance();
                FragmentTransaction myCardTransaction = getSupportFragmentManager().beginTransaction();
                myCardTransaction.replace(R.id.frameContainerChild, myCardFragment);
                myCardTransaction.addToBackStack(null);
                myCardTransaction.commit();
                break;
            case R.id.llProfile:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment profileFragment = ProfileFragment.newInstance();
                FragmentTransaction profileTransaction = getSupportFragmentManager().beginTransaction();
                profileTransaction.replace(R.id.frameContainerChild, profileFragment);
                profileTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                profileTransaction.addToBackStack(null);
                getSupportFragmentManager().executePendingTransactions();
                profileTransaction.commit();

                break;
            case R.id.ivMenu:
                hideKeyBoardImageView(ivMenu);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.llHome:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment homeFragment = HomeFragment.newInstance();
                FragmentTransaction homeTransaction = getSupportFragmentManager().beginTransaction();
                homeTransaction.replace(R.id.frameContainerChild, homeFragment);
                homeTransaction.addToBackStack(null);
                homeTransaction.commit();
                break;
            case R.id.llServices:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment servicesFragment = ServicesFragment.newInstance();
                FragmentTransaction servicesTransaction = getSupportFragmentManager().beginTransaction();
                servicesTransaction.replace(R.id.frameContainerChild, servicesFragment);
                servicesTransaction.addToBackStack(null);
                servicesTransaction.commit();
                break;
            case R.id.llAbout:
                drawerLayout.closeDrawer(GravityCompat.START);
                customAlert("This section is in under development.");
                break;
            case R.id.rlLogout:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                customTwoButtonAlert("Do want to logout?");
                break;
            case R.id.rlCreate:
                hideKeyBoardRelativeLayout(rlCreate);
                Fragment createcardFragment = CreateCardFragment.newInstance();
                FragmentTransaction createcardTransaction = getSupportFragmentManager().beginTransaction();
                createcardTransaction.replace(R.id.frameContainerChild, createcardFragment);
                createcardTransaction.addToBackStack(null);
                createcardTransaction.commit();
                break;
        }
    }

    private void customTwoButtonAlert(String s) {
        final CustomAlertWithTwoButton customAlertWithTwoButton=new CustomAlertWithTwoButton(this);
        if(!customAlertWithTwoButton.isShowing()){
            customAlertWithTwoButton.show();
            customAlertWithTwoButton.setCanceledOnTouchOutside(false);
            customAlertWithTwoButton.setCancelable(false);
            customAlertWithTwoButton.tvDesc.setText(s);
            customAlertWithTwoButton.btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithTwoButton.dismiss();
                }
            });
            customAlertWithTwoButton.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customAlertWithTwoButton.dismiss();
                    backPreviousActivity();
                }
            });
        }
    }

    private void backPreviousActivity() {
        myPreference.removeUser();
        CreateCardFragment.serviceID=null;
        CreateCardFragment.userTypeSelected=false;
        ProfileFragment.tvUserType=null;
        ProfileFragment.tvUsername=null;
        getCardResponseDataModel=null;
        cardDetailsResponseModel=null;
        layoutList.clear();
        galleryList.clear();
        viewCount=null;
        follow_status=0;
        follow_count=null;
        layoutUrl=null;
        myPreference.setLoad(false);
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
