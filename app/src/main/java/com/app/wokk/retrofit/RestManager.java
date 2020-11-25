package com.app.wokk.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static volatile RestManager sSoleInstance;

    //private constructor.
    private RestManager(){

        //Prevent form the reflection api.
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static RestManager getInstance() {
        //Double check locking pattern
        if (sSoleInstance == null) { //Check for the first time

            synchronized (RestManager.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sSoleInstance == null) sSoleInstance = new RestManager();
            }
        }

        return sSoleInstance;
    }

    private ApiService aPIService;

    public ApiService getService() {
        if (aPIService == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
//                    .addInterceptor(new ConnectivityInterceptor(MyApplication.getInstance()))
                    .connectTimeout(500, TimeUnit.SECONDS)
                    .readTimeout(500, TimeUnit.SECONDS)
                    .writeTimeout(500, TimeUnit.SECONDS)
                    .build();
// add your other interceptors …

// add logging as last interceptor
//            httpClient.addInterceptor(logging);  // <-- this is the important line!
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BaseUrl)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            aPIService = retrofit.create(ApiService.class);
        }
        return aPIService;
    }



    public ApiService getService(String baseUrl) {
        if (aPIService == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(500, TimeUnit.SECONDS)
                    .readTimeout(500, TimeUnit.SECONDS)
                    .writeTimeout(500, TimeUnit.SECONDS)
                    .build();
// add your other interceptors …

// add logging as last interceptor
//            httpClient.addInterceptor(logging);  // <-- this is the important line!
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            aPIService = retrofit.create(ApiService.class);
        }
        return aPIService;
    }

    public  boolean checkInternetConnection(Context context) {
        System.out.println("Internet////////////////////");


        ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=conMgr.getActiveNetworkInfo();

        System.out.println("Value....................n"+activeNetwork);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&
                conMgr.getActiveNetworkInfo().isConnected()) {
            System.out.println("returnInternet////////////////////");
            return true;
        } else {
            System.out.println("returnInternet////////////////////");

            System.out.println("Internet Connection Not Present");
            return false;
        }


    }
}
