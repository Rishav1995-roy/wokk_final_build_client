package com.app.wokk.mvp;

import com.app.wokk.model.OtpResponseModel;

import retrofit2.Response;

public class OtpModel {

    public interface View{
        void doSuccess(Response<OtpResponseModel> response);
        void doFailure(int code);
        void doError(String message);
    }

    public interface Presenter{
        void doCheckOtp(String otp,String phoneNumber,String userID);
    }
}
