package com.unipi.projects.smartalert.Services.Auth;

import android.app.Activity;

import com.unipi.projects.smartalert.Model.Auth.LoginRequest;
import com.unipi.projects.smartalert.Services.RetrofitService;

import io.reactivex.rxjava3.core.Single;

public class AuthService implements IAuthService{

    @Override
    public Single<AuthResult> Login(String email, String password) {

        RetrofitService retrofitService = new RetrofitService("http://192.168.1.75:12021");

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        IAuthHttp authHttp = RetrofitService.retrofit.create(IAuthHttp.class);

        Single<AuthResult> authCall = authHttp.Login(loginRequest);

        return authCall;
    }

    @Override
    public void Register(String email, String phone, String password, Activity activity) {

    }
}
