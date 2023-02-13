package com.unipi.projects.smartalert.Interfaces;

import com.unipi.projects.smartalert.Model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ITestApi {
    @POST("/auth/login")
    Call<LoginRequest> Login(@Body LoginRequest request);
}
