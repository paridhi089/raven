package dev.paridhi.raven.others;


import dev.paridhi.raven.model.ApiMessageModel;
import dev.paridhi.raven.model.ApiResponseModel;
import dev.paridhi.raven.model.MessageModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("/predict")
    Call<ApiResponseModel> getMessageType(@Body ApiMessageModel apiMessageModel);
}
