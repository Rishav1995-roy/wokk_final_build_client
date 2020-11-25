package com.app.wokk.retrofit;

import com.app.wokk.model.CardLiistForParticularServiceResponseModel;
import com.app.wokk.model.FontResponseModel;
import com.app.wokk.model.GetCardResponseModel;
import com.app.wokk.model.LoginResponseModel;
import com.app.wokk.model.OtpResponseModel;
import com.app.wokk.model.RegisterResponseModel;
import com.app.wokk.model.ResendResponseModel;
import com.app.wokk.model.ServiceResponseModelClass;
import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST(Constant.login)
    Call<LoginResponseModel> doLogin(@Body JsonElement jsonElement);

    @POST(Constant.Register)
    Call<RegisterResponseModel> doRegister(@Body JsonElement jsonElement);

    @POST(Constant.Verify_otp)
    Call<OtpResponseModel> verify_otp(@Body JsonElement jsonElement);

    @POST(Constant.Resend_otp)
    Call<ResendResponseModel> resend_otp(@Body JsonElement jsonElement);

    @POST(Constant.Get_Profile)
    Call<GetCardResponseModel> get_profile(@Body JsonElement jsonElement);

    @POST(Constant.Get_Service_list)
    Call<ServiceResponseModelClass> get_service(@Body JsonElement jsonElement);

    @POST(Constant.Create_Card)
    Call<ResponseBody>  create_card(@Body JsonElement jsonElement);

    @Multipart
    @POST(Constant.Add_Gallery)
    Call<ResponseBody> add_iamge(@Part(Constant.Add_Image.Api_USER)RequestBody apiuser,
                                 @Part(Constant.Add_Image.Api_Pass)RequestBody apipass,
                                 @Part(Constant.Add_Image.User_Id)RequestBody user_id,
                                 @Part MultipartBody.Part gallery_image,
                                 @Part(Constant.Add_Image.Gallery_Title)RequestBody gallery_title,
                                 @Part(Constant.Add_Image.Gallery_caption)RequestBody gallery_caption
    );

    @POST(Constant.Edit_Gallery)
    Call<ResponseBody> edit_gallery (@Body JsonElement jsonElement);

    @POST(Constant.Delete_Gallery)
    Call<ResponseBody> delete_gallery(@Body JsonElement jsonElement);

    @POST(Constant.Edit_Card)
    Call<ResponseBody> edit_card(@Body JsonElement jsonElement);

    @POST(Constant.Edit_profile)
    Call<ResponseBody> edit_profile(@Body JsonElement jsonElement);

    @POST(Constant.Card_List_For_Particular_service)
    Call<CardLiistForParticularServiceResponseModel> getCardListForParticularService(@Body JsonElement jsonElement);

    @POST(Constant.Follow_Card)
    Call<ResponseBody> doFollow(@Body JsonElement jsonElement);

    @POST(Constant.Font_Family)
    Call<FontResponseModel> getFontFamily(@Body JsonElement jsonElement);

    @POST(Constant.VIEWS)
    Call<ResponseBody> doViews(@Body JsonElement jsonElement);

    @POST(Constant.ADD_YOUTUBE_LINK)
    Call<ResponseBody>  addVideo(@Body JsonElement jsonElement);

    @POST(Constant.DELETE_YOUTUBE_LINK)
    Call<ResponseBody>  deleteVideo(@Body JsonElement jsonElement);

    @POST(Constant.FIREBASE_TOKEN_REGISTER)
    Call<ResponseBody>  doFirebaseTokenRegister(@Body JsonElement jsonElement);
}
