package com.unipi.projects.smartalert.Services.Auth;

import com.unipi.projects.smartalert.Model.Auth.LoginRequest;
import com.unipi.projects.smartalert.Model.Auth.RegisterRequest;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthHttp {
    @POST("/auth/login")
    Single<AuthResult> Login(@Body LoginRequest request);

    @POST("/auth/register")
    Single<AuthResult> Register(@Body RegisterRequest request);

//    @POST("/auth/logout")
//    Call<LoginRequest> Logout(@Body LogoutRequest request);
}
