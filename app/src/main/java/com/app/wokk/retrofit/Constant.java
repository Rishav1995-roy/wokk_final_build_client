package com.app.wokk.retrofit;

public class Constant {

    public static final String BaseUrl = "https://wokk.co.in/api/";

    public static final String apiuser="sarasij94";
    public static final String apipass="123";

    public static final String login="login";
    public static final String Register="register";
    public static final String Verify_otp="verify_otp";
    public static final String Resend_otp="resend_otp";
    public static final String Get_Profile="card";
    public static final String Create_Card="create_card";
    public static final String Get_Service_list="all_services";
    public static final String Add_Gallery="add_gallery";
    public static final String Edit_Gallery="edit_gallery";
    public static final String Delete_Gallery="delete_gallery";
    public static final String Edit_Card="edit_card";
    public static final String Edit_profile="edit_profile";
    public static final String Card_List_For_Particular_service="all_card_by_service";
    public static final String Follow_Card="follow_card";
    public static final String Verify_Conatct_For_Forgot_Password=""; //Due for Api Format
    public static final String Rest_Password=""; //Due for Api Format
    public static final String Font_Family="fonts_families";
    public static final String VIEWS="increase_view_count";
    public static final String ADD_YOUTUBE_LINK="add_youtube";
    public static final String DELETE_YOUTUBE_LINK="delete_youtube";
    public static final String FIREBASE_TOKEN_REGISTER="update_device_id";

    public interface Add_Image{
        String Api_USER="apiuser", Api_Pass="apipass",User_Id="user_id", Gallery_Image="gallery_image", Gallery_Title="gallery_title", Gallery_caption="gallery_caption";
    }
}
