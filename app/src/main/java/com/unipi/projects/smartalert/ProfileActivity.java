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
import android.widget.Spinner;
import android.widget.TextView;

import com.unipi.projects.smartalert.Services.Auth.AuthResult;
import com.unipi.projects.smartalert.Services.Events.EventResult;
import com.unipi.projects.smartalert.Services.Events.EventService;
import com.unipi.projects.smartalert.Services.Events.EventUserStatisticsResult;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupSpinnerLangSelection();
        onSpinnerChanged();

        // getting data from intent
        String email = getIntent().getStringExtra("email");
        String userId = getIntent().getStringExtra("userId");

        Button newEventButton = findViewById(R.id.newEventButton);

        newEventButton.setOnClickListener(
                (view) -> {
                    Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);

                    mainIntent.putExtra("email", email);
                    mainIntent.putExtra("userId", userId);

                    startActivity(mainIntent);
                }
        );

        TextView emailTextView = findViewById(R.id.emailTextView);

        emailTextView.setText(email);

        TextView earthquakeEventsCountTextView = findViewById(R.id.earthquakeEventsCount);
        TextView fireEventsCountTextView = findViewById(R.id.fireEventsCount);
        TextView floodEventsCountTextView = findViewById(R.id.floodEventsCount);

        Single<EventUserStatisticsResult> singleEventUserStatisticsResult;
        EventService eventService = new EventService();

        singleEventUserStatisticsResult = eventService.GetEventUserStatistics(userId);

        singleEventUserStatisticsResult
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<EventUserStatisticsResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull EventUserStatisticsResult eventUserStatisticsResult) {

                        earthquakeEventsCountTextView
                                .setText(String.valueOf(eventUserStatisticsResult.getEarthquakeEventsNum()));

                        fireEventsCountTextView
                                .setText(String.valueOf(eventUserStatisticsResult.getFireEventsNum()));

                        floodEventsCountTextView
                                .setText(String.valueOf(eventUserStatisticsResult.getFloodEventsNum()));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("ERR", e.getMessage());
                    }
                });

    }

    private void setupSpinnerLangSelection() {
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