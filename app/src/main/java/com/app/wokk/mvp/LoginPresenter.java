package com.app.wokk.mvp;

import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.LoginModelClass;
import com.app.wokk.model.LoginResponseModel;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginModel.Presenter {

    LoginModel.View view;

    public LoginPresenter(LoginModel.View view){
        this.view=view;
    }

    @Override
    public void doLogin(String email, String password) {
        ApiCredentialModel apiCredentialModel=new ApiCredentialModel();
        apiCredentialModel.apiuser= Constant.apiuser;
        apiCredentialModel.apipass=Constant.apipass;
        LoginModelClass loginModelClass=new LoginModelClass();
        loginModelClass.apiCredentialModel=apiCredentialModel;
        loginModelClass.username= Objects.requireNonNull(email);
        loginModelClass.password= Objects.requireNonNull(password);
        Gson gson=new Gson();
        JsonElement jsonElement=gson.toJsonTree(loginModelClass);
        Call<LoginResponseModel> login= RestManager.getInstance().getService().doLogin(jsonElement);
        login.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponseModel> call, @NotNull Response<LoginResponseModel> response) {
                try {
                    assert response.body() != null;
                    int code=response.body().code;
                    if(code == 1){
                        view.doSuccess(response);
                    }else if(code == 9){
                        view.doFailure(code);
                    }else{
                        view.doFailure(code);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    view.doValidation("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponseModel> call, @NotNull Throwable t) {
                view.doValidation("Oops, something went wrong!");
            }
        });
    }
}
