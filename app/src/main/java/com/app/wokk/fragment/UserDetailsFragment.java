package com.app.wokk.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.customAlert.EditProfileAlert;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.EditProfileClass;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseDataModel;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsFragment extends BaseFragment implements View.OnClickListener {

    public static UserDetailsFragment newInstance() {
        return new UserDetailsFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public LinearLayout llEdit,llProfile;
    public TextView etOrganisatiodescription,etName,etLastName,etEmail,etWhatsapp,etContact,etBio,tvGender,etAddress,etpincode,etOrganisationame,tvEdit,etValidity,tvCardValidityHeading;
    public ImageView ivEdit;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        myPreference = new MyPreference(getActivity());
        //ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
        init(rootView);
        return rootView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(View rootView) {
        tvCardValidityHeading = rootView.findViewById(R.id.tvCardValidityHeading);
        etOrganisatiodescription = rootView.findViewById(R.id.etOrganisatiodescription);
        etOrganisationame = rootView.findViewById(R.id.etOrganisationame);
        etValidity = rootView.findViewById(R.id.etValidity);
        tvGender = rootView.findViewById(R.id.tvGender);
        etpincode = rootView.findViewById(R.id.etpincode);
        etAddress = rootView.findViewById(R.id.etAddress);
        etLastName = rootView.findViewById(R.id.etLastName);
        llProfile = rootView.findViewById(R.id.llProfile);
        tvEdit = rootView.findViewById(R.id.tvEdit);
        ivEdit = rootView.findViewById(R.id.ivEdit);
        etBio = rootView.findViewById(R.id.etBio);
        etContact = rootView.findViewById(R.id.etContact);
        etWhatsapp = rootView.findViewById(R.id.etWhatsapp);
        etEmail = rootView.findViewById(R.id.etEmail);
        etName = rootView.findViewById(R.id.etName);
        llEdit = rootView.findViewById(R.id.llEdit);
        setData();
        clickEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setData(){
        //getLifecycle().addObserver(youtubePlayerView);
        if(ContainerActivity.getCardResponseDataModel.user_fname != null && !ContainerActivity.getCardResponseDataModel.user_fname.equals(""))
            etName.setText(ContainerActivity.getCardResponseDataModel.user_fname);
        else
            etName.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_lname != null && !ContainerActivity.getCardResponseDataModel.user_lname.equals(""))
            etLastName.setText(ContainerActivity.getCardResponseDataModel.user_lname);
        else
            etLastName.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_address != null && !ContainerActivity.getCardResponseDataModel.user_address.equals(""))
            etAddress.setText(ContainerActivity.getCardResponseDataModel.user_address);
        else
            etAddress.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_organization_desc != null && !ContainerActivity.getCardResponseDataModel.user_organization_desc.equals(""))
            etOrganisatiodescription.setText(ContainerActivity.getCardResponseDataModel.user_organization_desc);
        else
            etOrganisatiodescription.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_organization_name != null && !ContainerActivity.getCardResponseDataModel.user_organization_name.equals(""))
            etOrganisationame.setText(ContainerActivity.getCardResponseDataModel.user_organization_name);
        else
            etOrganisationame.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_organization_desc != null && !ContainerActivity.getCardResponseDataModel.user_organization_desc.equals(""))
            etOrganisatiodescription.setText(ContainerActivity.getCardResponseDataModel.user_organization_desc);
        else
            etOrganisatiodescription.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_pin != null && !ContainerActivity.getCardResponseDataModel.user_pin.equals(""))
            etpincode.setText(ContainerActivity.getCardResponseDataModel.user_pin);
        else
            etpincode.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_email != null && !ContainerActivity.getCardResponseDataModel.user_email.equals(""))
            etEmail.setText(ContainerActivity.getCardResponseDataModel.user_email);
        else
            etEmail.setText("");
        etContact.setText(ContainerActivity.getCardResponseDataModel.user_phone);
        etWhatsapp.setText(ContainerActivity.getCardResponseDataModel.user_phone);
        if(ContainerActivity.getCardResponseDataModel.user_bio != null && !ContainerActivity.getCardResponseDataModel.user_bio.equals(""))
            etBio.setText(ContainerActivity.getCardResponseDataModel.user_bio);
        else
            etBio.setText("Nothing is added about yourself.");
        if(ContainerActivity.getCardResponseDataModel.user_gender.equals("1")){
            tvGender.setText("Male");
        }else if(ContainerActivity.getCardResponseDataModel.user_gender.equals("2")){
            tvGender.setText("Female");
        }
        if(ContainerActivity.cardDetailsResponseModel !=  null) {
            tvCardValidityHeading.setVisibility(View.VISIBLE);
            etValidity.setVisibility(View.VISIBLE);
            if (ContainerActivity.validity_status) {
                String date = dateFormat(ContainerActivity.getCardResponseDataModel.user_card_valid_until);
                if (!date.equals("")) {
                    etValidity.setText(date);
                    etValidity.setTextColor(getResources().getColor(R.color.black));
                }
            } else {
                String date = dateFormat(ContainerActivity.getCardResponseDataModel.user_card_valid_until);
                if (!date.equals("")) {
                    etValidity.setText("Card expired on " + date);
                    etValidity.setTextColor(getResources().getColor(R.color.orange));
                }
            }
        }else{
            tvCardValidityHeading.setVisibility(View.GONE);
            etValidity.setVisibility(View.GONE);
        }

    }

    public String dateFormat(String date)  {
        String formattedDate="";
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date da = originalFormat.parse(date);
            formattedDate = targetFormat.format(da);
        }catch(Exception e){
            e.printStackTrace();
        }
        return formattedDate;
    }

    private void clickEvent() {
        llEdit.setOnClickListener(this);
        llProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llEdit:
                doEditDialog(ContainerActivity.getCardResponseDataModel);
                break;
            case R.id.llProfile:
                hideKeyBoardLinearlayout(llProfile);
                break;

        }

    }

    private void doEditDialog(GetCardResponseDataModel getCardResponseDataModel) {
        final EditProfileAlert editProfileAlert=new EditProfileAlert(Objects.requireNonNull(getContext()));
        editProfileAlert.show();
        if(ContainerActivity.getCardResponseDataModel.user_fname != null)
            editProfileAlert.etFirstName.setText(ContainerActivity.getCardResponseDataModel.user_fname);
        else
            editProfileAlert.etFirstName.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_lname != null)
            editProfileAlert.etLastname.setText(ContainerActivity.getCardResponseDataModel.user_lname);
        else
            editProfileAlert.etLastname.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_address != null)
            editProfileAlert.etAddress.setText(ContainerActivity.getCardResponseDataModel.user_address);
        else
            editProfileAlert.etAddress.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_organization_name != null)
            editProfileAlert.etOrganisationame.setText(ContainerActivity.getCardResponseDataModel.user_organization_name);
        else
            editProfileAlert.etOrganisationame.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_organization_name != null)
            editProfileAlert.etOrganisatiodescription.setText(ContainerActivity.getCardResponseDataModel.user_organization_name);
        else
            editProfileAlert.etOrganisatiodescription.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_pin != null)
            editProfileAlert.etPin.setText(ContainerActivity.getCardResponseDataModel.user_pin);
        else
            editProfileAlert.etPin.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_email != null)
            editProfileAlert.etEmail.setText(ContainerActivity.getCardResponseDataModel.user_email);
        else
            editProfileAlert.etEmail.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_bio != null)
            editProfileAlert.etBio.setText(ContainerActivity.getCardResponseDataModel.user_bio);
        else
            editProfileAlert.etBio.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_youtube != null)
            editProfileAlert.etYoutubeLink.setText(ContainerActivity.getCardResponseDataModel.user_youtube);
        else
            editProfileAlert.etYoutubeLink.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_organization_desc != null)
            editProfileAlert.etOrganisatiodescription.setText(ContainerActivity.getCardResponseDataModel.user_organization_desc);
        else
            editProfileAlert.etOrganisatiodescription.setText("");
        if(ContainerActivity.getCardResponseDataModel.user_gender.equals("1")){
            editProfileAlert.ivMale.setImageResource(R.drawable.check);
            editProfileAlert.ivMale.setTag("selected");
            editProfileAlert.ivFemale.setTag("unselected");
            editProfileAlert.genderSelected=true;
            editProfileAlert.ivFemale.setImageResource(R.drawable.uncheck);
        }else if(ContainerActivity.getCardResponseDataModel.user_gender.equals("2")){
            editProfileAlert.ivFemale.setImageResource(R.drawable.check);
            editProfileAlert.ivFemale.setTag("selected");
            editProfileAlert.ivMale.setTag("unselected");
            editProfileAlert.genderSelected=true;
            editProfileAlert.ivMale.setImageResource(R.drawable.uncheck);
        }
        editProfileAlert.ivMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editProfileAlert.ivMale.getTag().toString().toLowerCase().equals("selected")){
                    editProfileAlert.ivMale.setTag("unselected");
                    editProfileAlert.ivMale.setImageResource(R.drawable.uncheck);
                    editProfileAlert.genderSelected=false;
                }else if(editProfileAlert.ivMale.getTag().toString().toLowerCase().equals("unselected")){
                    editProfileAlert.ivMale.setImageResource(R.drawable.check);
                    editProfileAlert.ivMale.setTag("selected");
                    editProfileAlert.ivFemale.setTag("unselected");
                    editProfileAlert.genderSelected=true;
                    editProfileAlert.ivFemale.setImageResource(R.drawable.uncheck);
                }
            }
        });
        editProfileAlert.ivFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editProfileAlert.ivFemale.getTag().toString().toLowerCase().equals("selected")){
                    editProfileAlert.ivFemale.setTag("unselected");
                    editProfileAlert.ivFemale.setImageResource(R.drawable.uncheck);
                    editProfileAlert.genderSelected=false;
                }else if(editProfileAlert.ivFemale.getTag().toString().toLowerCase().equals("unselected")){
                    editProfileAlert.ivFemale.setImageResource(R.drawable.check);
                    editProfileAlert.ivFemale.setTag("selected");
                    editProfileAlert.ivMale.setTag("unselected");
                    editProfileAlert.genderSelected=true;
                    editProfileAlert.ivMale.setImageResource(R.drawable.uncheck);
                }
            }
        });
        editProfileAlert.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation(editProfileAlert)){
                    editProfileAlert.dismiss();
                    doUpdateprofile(editProfileAlert);
                }
            }
        });
        editProfileAlert.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoardLinearlayout(editProfileAlert.llEdit);
            }
        });
    }

    private void doUpdateprofile(EditProfileAlert editProfileAlert) {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        EditProfileClass editProfileClass=new EditProfileClass();
        editProfileClass.apiCredentialModel=apiCredentialModel;
        editProfileClass.user_id=myPreference.getUserID();
        editProfileClass.user_fname= Objects.requireNonNull(editProfileAlert.etFirstName.getText()).toString();
        editProfileClass.user_lname= "";
        editProfileClass.user_address= Objects.requireNonNull(editProfileAlert.etAddress.getText()).toString();
        editProfileClass.user_pin= Objects.requireNonNull(editProfileAlert.etPin.getText()).toString();
        editProfileClass.user_email= Objects.requireNonNull(editProfileAlert.etEmail.getText()).toString();
        if(editProfileAlert.ivMale.getTag().toString().toLowerCase().equals("selected")){
            editProfileClass.user_gender="1";
        }else if(editProfileAlert.ivFemale.getTag().toString().toLowerCase().equals("selected")){
            editProfileClass.user_gender="2";
        }
        editProfileClass.user_organization_name= Objects.requireNonNull(editProfileAlert.etOrganisationame.getText()).toString();
        editProfileClass.user_youtube= Objects.requireNonNull(editProfileAlert.etYoutubeLink.getText()).toString();
        editProfileClass.user_organization_desc= Objects.requireNonNull(editProfileAlert.etOrganisatiodescription.getText()).toString();
        editProfileClass.user_bio= Objects.requireNonNull(editProfileAlert.etBio.getText()).toString();
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(editProfileClass);
        Call<ResponseBody> doUpdate= RestManager.getInstance().getService().edit_profile(jsonElement);
        doUpdate.enqueue(new Callback<ResponseBody>() {
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
                        ContainerActivity.cardDetailsResponseModel=response.body().card_details;
                        ContainerActivity.layoutList=response.body().all_layouts;
                        ContainerActivity.getCardResponseDataModel=response.body().user_details;
                        ContainerActivity.youtubeDetailsModelArrayList=response.body().youtube_details;
                        ContainerActivity.follow_status=response.body().follow_status;
                        ContainerActivity.follow_count=response.body().no_of_followers;
                        ContainerActivity.validity_status=response.body().validity_status;
                        ContainerActivity.validityDate=response.body().user_details.user_card_valid_until;
                        ProfileFragment.tvAddress.setText(ContainerActivity.getCardResponseDataModel.user_address+" - "+ ContainerActivity.getCardResponseDataModel.user_pin);
                        ProfileFragment.tvUsername.setText(ContainerActivity.getCardResponseDataModel.user_fname+" "+ContainerActivity.getCardResponseDataModel.user_lname);
                        Fragment newFragment = ProfileFragment.newInstance();
                        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameContainerChild, newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        customAlert("Your profile has been updated successfully");
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
            //ProfileFragment.rlCard.setBackgroundDrawable(result);
        }
    }

    private boolean validation(EditProfileAlert editProfileAlert) {
        if (Objects.requireNonNull(editProfileAlert.etFirstName.getText()).toString().isEmpty()) {
            customAlert("Please enter your first name!");
            editProfileAlert.etFirstName.requestFocus();
            return false;
        }
        /*if (Objects.requireNonNull(editProfileAlert.etLastname.getText()).toString().isEmpty()) {
            customAlert("Please enter your last name!");
            editProfileAlert.etLastname.requestFocus();
            return false;
        }*/
        if (Objects.requireNonNull(editProfileAlert.etAddress.getText()).toString().isEmpty()) {
            customAlert("Please enter your address!");
            editProfileAlert.etAddress.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(editProfileAlert.etPin.getText()).toString().isEmpty()) {
            customAlert("Please enter your pincode!");
            editProfileAlert.etPin.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(editProfileAlert.etEmail.getText()).toString().isEmpty()) {
            customAlert("Please enter your email address!");
            editProfileAlert.etEmail.requestFocus();
            return false;
        }
        if (Objects.requireNonNull(editProfileAlert.etOrganisationame.getText()).toString().isEmpty()) {
            customAlert("Please enter your organasation name!");
            editProfileAlert.etOrganisationame.requestFocus();
            return false;
        }
        if(!editProfileAlert.genderSelected){
            customAlert("Please select gender");
            return false;
        }
        if (Objects.requireNonNull(editProfileAlert.etBio.getText()).toString().isEmpty()) {
            customAlert("Please enter about yourself!");
            editProfileAlert.etBio.requestFocus();
            return false;
        }
        return true;
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
