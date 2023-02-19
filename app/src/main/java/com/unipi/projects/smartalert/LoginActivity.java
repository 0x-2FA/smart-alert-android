package com.unipi.projects.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.projects.smartalert.Services.Auth.AuthResult;
import com.unipi.projects.smartalert.Services.Auth.AuthService;

import java.util.Locale;

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
        setupSpinnerSelection();
        onSpinnerChanged();

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);

        TextView registerLink = findViewById(R.id.registerLink);

        loginButton.setOnClickListener(
                (view) -> {
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    if(email.isEmpty()) {
                        emailEditText.setError(LoginActivity.this.getResources().getString(R.string.email_required));
                        return;
                    }

                    if(password.isEmpty()) {
                        passwordEditText.setError(LoginActivity.this.getResources().getString(R.string.password_required));
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

                                    Intent profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);

                                    profileIntent.putExtra("email", authResult.getEmail());
                                    profileIntent.putExtra("userId", authResult.getUserId());

                                    startActivity(profileIntent);
                                    LoginActivity.this.finish();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    Log.e("WOKRS", e.getMessage());
                                    if(e instanceof HttpException) {
                                        HttpException error = (HttpException) e;
                                        String errorResponse = error.response().message();

                                        if(errorResponse.contains("Bad")) {

                                            Toast.makeText(getApplicationContext(),
                                                    LoginActivity.this.getResources().getString(R.string.bad_request), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    Toast.makeText(getApplicationContext(),
                                            LoginActivity.this.getResources().getString(R.string.failed_server), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
        );

        registerLink.setOnClickListener(
                (view) -> {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
        });
    }

    private void setupSpinnerSelection() {
        Spinner spinner = findViewById(R.id.selectLangSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lang_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    private void onSpinnerChanged() {
        Spinner spinner = findViewById(R.id.selectLangSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getSelectedItemPosition() == 1)
                {
                     changeLocale("en");
                }
                else if(adapterView.getSelectedItemPosition() == 2)
                {
                    changeLocale("el");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void changeLocale(String language) {
        Locale locale = new Locale(language);
        Resources resources = this.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        Locale.setDefault(locale);

        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

       resources.updateConfiguration(config, displayMetrics);
    }

}