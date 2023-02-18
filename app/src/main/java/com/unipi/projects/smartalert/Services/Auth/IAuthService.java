package com.unipi.projects.smartalert.Services.Auth;

import android.app.Activity;

import io.reactivex.rxjava3.core.Single;

public interface IAuthService {
    public Single<AuthResult> Login(String email, String password);
    public Single<AuthResult> Register(String email, String phone, String password);
}
