package dev.paridhi.raven.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import dev.paridhi.raven.R;
import dev.paridhi.raven.databinding.ActivityApiTestBinding;
import dev.paridhi.raven.model.ApiMessageModel;
import dev.paridhi.raven.model.ApiResponseModel;
import dev.paridhi.raven.others.ApiInterface;
import dev.paridhi.raven.others.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTestActivity extends AppCompatActivity {


    ActivityApiTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityApiTestBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        binding.apiBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.apiMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.apitestMessage.getText().toString();
                ApiMessageModel apiMessageModel=new ApiMessageModel(message);

              Call<ApiResponseModel> call=RetrofitClient.getInstance().getApi().getMessageType(apiMessageModel);

              call.enqueue(new Callback<ApiResponseModel>() {
                  @Override
                  public void onResponse(Call<ApiResponseModel> call, Response<ApiResponseModel> response) {
                      try {
                          String s=response.body().getPrediction();
                          Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                          binding.apiResult.setText(s);
                      }catch (Exception e)
                      {
                          Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                      }


                  }

                  @Override
                  public void onFailure(Call<ApiResponseModel> call, Throwable t) {
                      Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                  }
              });





            }
        });
    }
}