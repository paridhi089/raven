package dev.paridhi.raven.others;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static RetrofitClient mInstance;
    public static Retrofit retrofit;

    //private static final String BASEURL="http://15.10.30.62:8000";
    private static final String BASEURL="https://spam-detection-api-1yq6.onrender.com";

    private RetrofitClient(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASEURL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
    }
    public static synchronized RetrofitClient getInstance(){
        if(mInstance==null)
        {
            mInstance=new RetrofitClient();
        }
        return mInstance;

    }
    public  ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);
    }

}
