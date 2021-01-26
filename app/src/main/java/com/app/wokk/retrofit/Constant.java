package com.app.wokk.retrofit;

public class Constant {

    static {
        System.loadLibrary("Wokk");
    }


    private native static String getUsername();

    private native static String getPassword();

    private native static String getBaseURL();

    public static final String BaseUrl = getBaseURL();
    public static final String apiuser = getUsername();
    public static final String apipass = getPassword();

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
    public static final String Verify_Conatct_For_Forgot_Password="change_password";
    public static final String Reset_Password="reset_password";
    public static final String Font_Family="fonts_families";
    public static final String VIEWS="increase_view_count";
    public static final String ADD_YOUTUBE_LINK="add_youtube";
    public static final String DELETE_YOUTUBE_LINK="delete_youtube";
    public static final String FIREBASE_TOKEN_REGISTER="update_device_id";

    public interface Add_Image{
        String Api_USER="apiuser", Api_Pass="apipass",User_Id="user_id", Gallery_Image="gallery_image", Gallery_Title="gallery_title", Gallery_caption="gallery_caption";
    }

    public interface CreateCard{
        String Api_USER="apiuser", Api_Pass="apipass",User_Id="user_id", First_Name="fname", Last_Name="lname", Address="address",Pin="pin",Email="email",Gender="gender",Service_ID="service",OrganisationName="org_name",OrganisationDescription="org_desc",Card_Image_Url="card_image";
    }

    public interface EditCard{
        String Api_USER="apiuser", Api_Pass="apipass",User_Id="user_id", Layout_ID="card_layout_id", Org_Color="card_org_color", Org_Font="card_org_font",Org_top="card_org_top_mob",Org_left="card_org_left_mob",Org_show="card_org_show",Name_color="card_name_color",Name_font="card_name_font",Name_top="card_name_top_mob",Name_left="card_name_left_mob",Name_show="card_name_show", Address_color="card_address_color",Address_font="card_address_font", Address_top="card_address_top_mob", Address_left="card_address_left_mob", Address_show="card_address_show",Email_color="card_email_color",Email_font="card_email_font",Email_top="card_email_top_mob",Email_left="card_email_left_mob",Email_show="card_email_show",Phone_color="card_phone_color",Phone_font="card_phone_font",Phone_top="card_phone_top_mob", Phone_left="card_phone_left_mob",Phone_show="card_phone_show", User_fname="user_fname", User_lname="user_lname", User_address="user_address",User_email="user_email",User_org_name="user_organization_name",Border_color="card_border_color",Phone_fontsize="card_phone_fontsize_mob",Address_fontsize="card_address_fontsize_mob",Email_fontsize="card_email_fontsize_mob",Name_fontsize="card_name_fontsize_mob",Org_fontsize="card_org_fontsize_mob",Card_Image_url="card_image",Inside_Image_Url="inside_card_image",Inside_Image_top="inside_image_top_mob",Inside_image_left="inside_image_left_mob",Inside_image_change="inside_image_change";
    }
}
