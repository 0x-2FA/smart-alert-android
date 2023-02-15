package com.unipi.projects.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unipi.projects.smartalert.Model.Auth.AuthResponse;
import com.unipi.projects.smartalert.Services.Auth.AuthResult;
import com.unipi.projects.smartalert.Services.Auth.AuthService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(
                (view) -> {
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    if(email.isEmpty()) {
                        emailEditText.setError("Email field is required");
                        return;
                    }

                    if(password.isEmpty()) {
                        passwordEditText.setError("Password field is required");
                        return;
                    }

                    AuthService authService = new AuthService();

                    Single<AuthResult> singleAuthResult = authService.Login(email, password);

                    singleAuthResult
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                            new SingleObserver<AuthResult>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull AuthResult authResult) {
                                    authResult.authResponse = new AuthResponse();
                                    authResult.authResponse.setEmail(email);

                                    Log.i("WOKRS", authResult.authResponse.getEmail());

                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                                    mainIntent.putExtra("email", authResult.authResponse.getEmail());

                                    startActivity(mainIntent);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.e("WOKRS", e.getMessage());
                                    if(e instanceof HttpException) {
                                        HttpException error = (HttpException) e;
                                        String errorResponse = error.response().message();


                                        if(errorResponse.contains("Bad")) {

                                            Toast.makeText(getApplicationContext(),
                                                    "User not found", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Log.e("WORKS", errorResponse);

                                    }

                                    Toast.makeText(getApplicationContext(),
                                            "Failed to connect to server", Toast.LENGTH_SHORT).show();

                                }
                            }
                    );
                }
        );
    }
}