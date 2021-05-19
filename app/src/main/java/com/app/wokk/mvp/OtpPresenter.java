package com.app.wokk.mvp;

import com.app.wokk.model.ApiCredentialModel;
import com.app.wokk.model.OtpModelClass;
import com.app.wokk.model.OtpResponseModel;
import com.app.wokk.retrofit.Constant;
import com.app.wokk.retrofit.RestManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpPresenter implements OtpModel.Presenter {

    OtpModel.View view;

    public OtpPresenter(OtpModel.View view){
        this.view=view;
    }

    @Override
    public void doCheckOtp(String otp,String phoneNumber,String userID) {
        ApiCredentialModel apiCredentialModel = new ApiCredentialModel();
        apiCredentialModel.apiuser = Constant.apiuser;
        apiCredentialModel.apipass = Constant.apipass;
        OtpModelClass otpModelClass = new OtpModelClass();
        otpModelClass.apiCredentialModel = apiCredentialModel;
        otpModelClass.otp_value = otp;
        otpModelClass.phone = phoneNumber;
        otpModelClass.user_id = userID;
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(otpModelClass);
        Call<OtpResponseModel> doVerifyotp = RestManager.getInstance().getService().verify_otp(jsonElement);
        doVerifyotp.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<OtpResponseModel> call, @NotNull Response<OtpResponseModel> response) {
                try {
                    assert response.body() != null;
                    int code = response.body().code;
                    if(code == 1){
                       view.doSuccess(response);
                    }else{
                        view.doFailure(code);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.doError("Oops, something went wrong!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<OtpResponseModel> call, @NotNull Throwable t) {
                view.doError("Internal server error. Please try after few minutes.");
            }
        });
    }
}
