package com.unipi.projects.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.unipi.projects.smartalert.Interfaces.ITestApi;
import com.unipi.projects.smartalert.Model.LoginRequest;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(
                (view) -> {
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.1.75:12021")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();

                    ITestApi service = retrofit.create(ITestApi.class);
                    LoginRequest request = new LoginRequest();

                    request.setEmail("talepis@unipi.gr");
                    request.setPassword("12345Ta!");

                    Call<LoginRequest> call  = service.Login(request);

                    Log.i("call ", call .toString());

                    call.enqueue(new Callback<LoginRequest>() {
                        @Override
                        public void onResponse(
                                Call<LoginRequest> call,
                                Response<LoginRequest> response) {
                            Log.i("res", "Sent successfully!");
                            Log.i("res", response.body().getEmail());
                        }

                        @Override
                        public void onFailure(
                                Call<LoginRequest> call,
                                Throwable t) {
                            Log.i("res", "Something went wrong!");
                            Log.i("res", t.getMessage());

                        }
                    });

                    Toast.makeText(getApplicationContext(),
                            "Clicked", Toast.LENGTH_SHORT).show();
                }
        );
    }
}