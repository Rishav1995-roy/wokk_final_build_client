package com.app.wokk.mvp;

import com.app.wokk.model.LoginResponseModel;

import retrofit2.Response;

public class LoginModel {

    public interface View{
        public void doSuccess(Response<LoginResponseModel> response);
        public void doFailure(int code);
        public void doValidation(String messsage);
    }

    public interface Presenter{
        public void doLogin(String email,String password);
    }

}
