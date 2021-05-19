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

    @Multipart
    @POST(Constant.Create_Card)
    Call<ResponseBody>  create_card(@Part(Constant.CreateCard.Api_USER)RequestBody apiuser,
                                    @Part(Constant.CreateCard.Api_Pass)RequestBody apipass,
                                    @Part(Constant.CreateCard.User_Id)RequestBody user_id,
                                    @Part(Constant.CreateCard.First_Name)RequestBody fname,
                                    @Part(Constant.CreateCard.Last_Name)RequestBody lname,
                                    @Part(Constant.CreateCard.Address)RequestBody address,
                                    @Part(Constant.CreateCard.Pin)RequestBody pin,
                                    @Part(Constant.CreateCard.Email)RequestBody email,
                                    @Part(Constant.CreateCard.Gender)RequestBody gender,
                                    @Part(Constant.CreateCard.Service_ID)RequestBody service,
                                    @Part(Constant.CreateCard.OrganisationName)RequestBody org_name,
                                    @Part(Constant.CreateCard.OrganisationDescription)RequestBody org_desc,
                                    @Part MultipartBody.Part card_image);

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

    @Multipart
    @POST(Constant.Edit_Card)
    Call<ResponseBody> edit_card(@Part(Constant.EditCard.Api_USER)RequestBody apiuser,
                                 @Part(Constant.EditCard.Api_Pass)RequestBody apipass,
                                 @Part(Constant.EditCard.User_Id)RequestBody user_id,
                                 @Part(Constant.EditCard.Layout_ID)RequestBody card_layout_id,
                                 @Part(Constant.EditCard.Org_Color)RequestBody card_org_color,
                                 @Part(Constant.EditCard.Org_Font)RequestBody card_org_font,
                                 @Part(Constant.EditCard.Org_top)RequestBody card_org_top_mob,
                                 @Part(Constant.EditCard.Org_left)RequestBody card_org_left_mob,
                                 @Part(Constant.EditCard.Org_show)RequestBody card_org_show,
                                 @Part(Constant.EditCard.Name_color)RequestBody card_name_color,
                                 @Part(Constant.EditCard.Name_font)RequestBody card_name_font,
                                 @Part(Constant.EditCard.Name_top)RequestBody card_name_top_mob,
                                 @Part(Constant.EditCard.Name_left)RequestBody card_name_left_mob,
                                 @Part(Constant.EditCard.Name_show)RequestBody card_name_show,
                                 @Part(Constant.EditCard.Address_color)RequestBody card_address_color,
                                 @Part(Constant.EditCard.Address_font)RequestBody card_address_font,
                                 @Part(Constant.EditCard.Address_top)RequestBody card_address_top_mob,
                                 @Part(Constant.EditCard.Address_left)RequestBody card_address_left_mob,
                                 @Part(Constant.EditCard.Address_show)RequestBody card_address_show,
                                 @Part(Constant.EditCard.Email_color)RequestBody card_email_color,
                                 @Part(Constant.EditCard.Email_font)RequestBody card_email_font,
                                 @Part(Constant.EditCard.Email_top)RequestBody card_email_top_mob,
                                 @Part(Constant.EditCard.Email_left)RequestBody card_email_left_mob,
                                 @Part(Constant.EditCard.Email_show)RequestBody card_email_show,
                                 @Part(Constant.EditCard.Phone_color)RequestBody card_phone_color,
                                 @Part(Constant.EditCard.Phone_font)RequestBody card_phone_font,
                                 @Part(Constant.EditCard.Phone_top)RequestBody card_phone_top_mob,
                                 @Part(Constant.EditCard.Phone_left)RequestBody card_phone_left_mob,
                                 @Part(Constant.EditCard.Phone_show)RequestBody card_phone_show,
                                 @Part(Constant.EditCard.User_fname)RequestBody user_fname,
                                 @Part(Constant.EditCard.User_lname)RequestBody user_lname,
                                 @Part(Constant.EditCard.User_address)RequestBody user_address,
                                 @Part(Constant.EditCard.User_email)RequestBody user_email,
                                 @Part(Constant.EditCard.User_org_name)RequestBody user_organization_name,
                                 @Part(Constant.EditCard.Border_color)RequestBody card_border_color,
                                 @Part(Constant.EditCard.Phone_fontsize)RequestBody card_phone_fontsize_mob,
                                 @Part(Constant.EditCard.Address_fontsize)RequestBody card_address_fontsize_mob,
                                 @Part(Constant.EditCard.Email_fontsize)RequestBody card_email_fontsize_mob,
                                 @Part(Constant.EditCard.Name_fontsize)RequestBody card_name_fontsize_mob,
                                 @Part(Constant.EditCard.Org_fontsize)RequestBody card_org_fontsize_mob,
                                 @Part MultipartBody.Part card_image,
                                 @Part(Constant.EditCard.Inside_Image_top)RequestBody inside_image_top_mob,
                                 @Part(Constant.EditCard.Inside_image_left)RequestBody inside_image_left_mob,
                                 @Part(Constant.EditCard.Inside_image_change)RequestBody inside_image_change,
                                 @Part MultipartBody.Part inside_card_image);

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
