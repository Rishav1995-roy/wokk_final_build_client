package com.app.wokk.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wokk.R;
import com.app.wokk.activity.ContainerActivity;
import com.app.wokk.activity.NetworkCheck;
import com.app.wokk.adapter.GalleryAdapter;
import com.app.wokk.combine.BaseFragment;
import com.app.wokk.customAlert.CustomAlertWithOneButton;
import com.app.wokk.customAlert.CustomPhotoGalleryAlert;
import com.app.wokk.customAlert.CustomPictureDetailsAlert;
import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.DeleteGalleryClass;
import com.app.wokk.model.EditGalleryClass;
import com.app.wokk.model.GalleryResponseModel;
import com.app.wokk.model.GetCardClass;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.preference.MyPreference;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

public class UserGalleryFragment extends BaseFragment  implements View.OnClickListener{

    public static UserGalleryFragment newInstance() {
        return new UserGalleryFragment();
    }

    public View rootView;
    public MyPreference myPreference;
    public RecyclerView rvGallery;
    public TextView tvEmptyText;
    public LinearLayout llUpload;
    String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public Uri uri;
    public File pic;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        rootView=inflater.inflate(R.layout.fragment_user_gallery,container,false);
        myPreference=new MyPreference(getActivity());
        //ContainerActivity.btnCreateVisitingCard.setVisibility(View.GONE);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        llUpload=rootView.findViewById(R.id.llUpload);
        rvGallery=rootView.findViewById(R.id.rvGallery);
        tvEmptyText=rootView.findViewById(R.id.tvEmptyText);
        setupAdapter(ContainerActivity.galleryList);
        clickEvent();
    }

    private void clickEvent() {
        llUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.llUpload){
            hideKeyBoardLinearlayout(llUpload);
            requestPermission();
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
                customGalleryCameraAlert();
            } else {
                requestPermission();
            }
        }
    }

    private void customGalleryCameraAlert() {
        final CustomPhotoGalleryAlert customPhotoGalleryAlert = new CustomPhotoGalleryAlert(Objects.requireNonNull(getActivity()));
        if (!customPhotoGalleryAlert.isShowing()) {
            customPhotoGalleryAlert.show();
            customPhotoGalleryAlert.llCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customPhotoGalleryAlert.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 200);

                }
            });
            customPhotoGalleryAlert.llGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customPhotoGalleryAlert.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 201);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    //onOpenCamera();
                    assert data != null;
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    assert bitmap != null;
                    uri = getImageUri(Objects.requireNonNull(getContext()), bitmap);
                    pic = new File(getRealPathFromURI(uri));
                    pictureShowAlert(pic,bitmap);
                }
                break;
            case 201:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            //cardLogo.setVisibility(View.VISIBLE);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            BitmapFactory.Options Options = new BitmapFactory.Options();
                            Options.inSampleSize = 4;
                            Options.inJustDecodeBounds = false;
                            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, Options);
                            //cardLogo.setImageBitmap(bitmap);
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            //uri=getImageUri(Objects.requireNonNull(getContext()),bitmap);
                            pic = new File(getRealPathFromURI(selectedImage));
                            cursor.close();
                            pictureShowAlert(pic,bitmap);
                        }
                    }

                }
                break;
        }
    }

    private void pictureShowAlert(final File pic, Bitmap bitmap) {
        final CustomPictureDetailsAlert customPictureDetailsAlert=new CustomPictureDetailsAlert(Objects.requireNonNull(getActivity()));
        customPictureDetailsAlert.show();
        /*if(pic.exists()){
            customPictureDetailsAlert.ivPreview.setImageURI(Uri.fromFile(pic));
        }*/
        customPictureDetailsAlert.ivPreview.setImageBitmap(bitmap);
        customPictureDetailsAlert.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customPictureDetailsAlert.dismiss();
            }
        });
        customPictureDetailsAlert.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation(customPictureDetailsAlert)){
                    customPictureDetailsAlert.dismiss();
                    boolean networkCheck = NetworkCheck.getInstant(getActivity()).isConnectingToInternet();
                    if (networkCheck) {
                        addImage(customPictureDetailsAlert.etCaption,customPictureDetailsAlert.etTitle,pic);
                    }else{
                        customAlert(getResources().getString(R.string.noInternetText));
                    }
                }
            }
        });
    }

    private void addImage(TextInputEditText etCaption, TextInputEditText etTitle, File pic) {
        showRotateDialog();
        RequestBody apiUser=RequestBody.create(MediaType.parse("multipart/form-data"), Constant.apiuser);
        RequestBody apiPass=RequestBody.create(MediaType.parse("multipart/form-data"), Constant.apipass);
        RequestBody userId=RequestBody.create(MediaType.parse("multipart/form-data"), myPreference.getUserID());
        RequestBody galleryTitle=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etTitle.getText()).toString());
        RequestBody galleryCaption=RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(etCaption.getText()).toString());
        MultipartBody.Part image = null;
        RequestBody image_path = null;
        if (pic != null) {
            RequestBody propertyImage = RequestBody.create(MediaType.parse("multipart/form-data"), pic);
            image = MultipartBody.Part.createFormData("gallery_image", "image.jpg", propertyImage);
        }
        Call<ResponseBody> addImage= RestManager.getInstance().getService().add_iamge(apiUser,apiPass,userId,image,galleryTitle,galleryCaption);
        addImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile(1);
                    }else if(jsonObject.optInt("code") == 9){
                        hideRotateDialog();
                        customAlert("Authentication error occurred.");
                    }else{
                        hideRotateDialog();
                        customAlert("Oops, something went wrong!");
                    }
                }catch (Exception e) {
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

    private void getProfile(final int id) {
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
                        ContainerActivity.youtubeDetailsModelArrayList=response.body().youtube_details;
                        ContainerActivity.layoutList=response.body().all_layouts;
                        ContainerActivity.getCardResponseDataModel=response.body().user_details;
                        ContainerActivity.follow_status=response.body().follow_status;
                        ContainerActivity.follow_count=response.body().no_of_followers;
                        ContainerActivity.validity_status=response.body().validity_status;
                        ContainerActivity.validityDate=response.body().user_details.user_card_valid_until;
                        if(id == 1){
                            customAlert("Image has been added successfully.");
                            setupAdapter(ContainerActivity.galleryList);
                        }else if(id == 2){
                            customAlert("Iamge has been deleted successfully.");
                            setupAdapter(ContainerActivity.galleryList);
                        }else{
                            customAlert("Image has been edited successfully.");
                            setupAdapter(ContainerActivity.galleryList);
                        }
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

    private boolean validation(CustomPictureDetailsAlert customPictureDetailsAlert) {
        if(Objects.requireNonNull(customPictureDetailsAlert.etTitle.getText()).toString().isEmpty()){
            customAlert("Please enter a title for the image.");
            customPictureDetailsAlert.etTitle.requestFocus();
            return false;
        }
        if(Objects.requireNonNull(customPictureDetailsAlert.etCaption.getText()).toString().isEmpty()){
            customAlert("Please enter caption for the image.");
            customPictureDetailsAlert.etCaption.requestFocus();
            return false;
        }
        return true;
    }

    public void customAlert(String s) {
        final CustomAlertWithOneButton customAlertWithOneButton=new CustomAlertWithOneButton(Objects.requireNonNull(getActivity()));
        if(!customAlertWithOneButton.isShowing()){
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

    Uri getImageUri(Context inContext, Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", "");
        return Uri.parse(path);
    }

    String getRealPathFromURI(Uri uri){
        String path = "";
        if (Objects.requireNonNull(getActivity()).getContentResolver() != null) {
            Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void setupAdapter(ArrayList<GalleryResponseModel> galleryImageList) {
        if(galleryImageList.size() > 0){
            rvGallery.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
            rvGallery.setLayoutManager(new LinearLayoutManager(getActivity()));
            GalleryAdapter galleryAdapter=new GalleryAdapter(this,galleryImageList);
            rvGallery.setAdapter(galleryAdapter);
        }else{
            rvGallery.setVisibility(View.GONE);
            tvEmptyText.setVisibility(View.VISIBLE);
        }
    }

    public void deleteImage(String position, int i) {
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser=Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        DeleteGalleryClass deleteGalleryClass=new DeleteGalleryClass();
        deleteGalleryClass.apiCredentialModel=apiCredentialModel;
        deleteGalleryClass.gallery_id=position;
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(deleteGalleryClass);
        Call<ResponseBody> doDelete=RestManager.getInstance().getService().delete_gallery(jsonElement);
        doDelete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                try{
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile(2);
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

    public void editGallery(String title, String caption, String gallery_id){
        showRotateDialog();
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser=Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        EditGalleryClass editGalleryClass=new EditGalleryClass();
        editGalleryClass.apiCredentialModel=apiCredentialModel;
        editGalleryClass.gallery_id=gallery_id;
        editGalleryClass.gallery_title=title;
        editGalleryClass.gallery_caption=caption;
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(editGalleryClass);
        Call<ResponseBody> doEdit=RestManager.getInstance().getService().edit_gallery(jsonElement);
        doEdit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call,@NotNull Response<ResponseBody> response) {
                try{
                    assert response.body() != null;
                    String val = response.body().string();
                    JSONObject jsonObject = new JSONObject(val);
                    if (jsonObject.optInt("code") == 1) {
                        getProfile(3);
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
}
