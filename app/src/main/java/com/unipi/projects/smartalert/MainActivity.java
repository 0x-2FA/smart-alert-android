package com.unipi.projects.smartalert;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.unipi.projects.smartalert.Services.Events.EventResult;
import com.unipi.projects.smartalert.Services.Events.EventService;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private LocationManager _locationManager;
    private LocationListener _locationListener;

    private Double _lat;
    private Double _long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSpinnerLangSelection();
        onSpinnerChanged();

        // getting data from intent
        String email = getIntent().getStringExtra("email");
        String userId = getIntent().getStringExtra("userId");

        setupSpinnerSelection();

        locationRequest();

        _locationListener = setLocationListener();
        _locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        EditText editTextMultiLine = findViewById(R.id.editTextMultiLine);

        Button sendEventButton = findViewById(R.id.sendEventButton);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, _locationListener);

            Log.i("MOT", "ENVOKED locationManager");
        }

        sendEventButton.setOnClickListener(
                (view) -> {
                    Spinner spinner = findViewById(R.id.selectTypeSpinner);
                    String selectedType = spinner.getSelectedItem().toString();

                    Single<EventResult> singleEventResult;
                    EventService eventService = new EventService();

                    if(editTextMultiLine.getText().toString().isEmpty()) {
                        singleEventResult = eventService
                                .SendEvent(
                                    selectedType,
                                    _lat.toString(),
                                    _long.toString(),
                                    userId
                                );

                    }
                    else {

                        String comment = editTextMultiLine.getText().toString();

                        singleEventResult = eventService
                                .SendEvent(
                                        selectedType,
                                       _lat.toString(),
                                       _long.toString(),
                                        comment,
                                        userId
                                );
                    }

                    singleEventResultSubscription(singleEventResult);

                    String toastText = this.getResources().getString(R.string.location_sent_successfully);

                    Toast.makeText(getApplicationContext(),
                            toastText, Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void setupSpinnerSelection() {
        Spinner spinner = findViewById(R.id.selectTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    private void locationRequest() {
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    Button sendEventButton = findViewById(R.id.sendEventButton);

                    if (isGranted) {
                        sendEventButton.setClickable(true);
                        sendEventButton.setLongClickable(true);

                    } else {
                        sendEventButton.setClickable(false);
                        sendEventButton.setLongClickable(false);

                        String title = this.getResources().getString(R.string.declined_location_request_title);
                        String message = this.getResources().getString(R.string.declined_location_request_message);

                        showMessage(title, message);
                    }
                });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();
    }

    private LocationListener setLocationListener() {
        return  new LocationListener() {
            @Override
            public void onLocationChanged(@androidx.annotation.NonNull Location location) {
                _lat = location.getLatitude();
                _long = location.getLongitude();
            }

            @Override
            public void onFlushComplete(int requestCode) {
                //LocationListener.super.onFlushComplete(requestCode);
            }

            @Override
            public void onProviderEnabled(@androidx.annotation.NonNull String provider) {
                //LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@androidx.annotation.NonNull String provider) {
                //LocationListener.super.onProviderDisabled(provider);
            }
        };
    }

    private void singleEventResultSubscription(@NonNull Single<EventResult> singleEventResult) {
        singleEventResult
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new SingleObserver<EventResult>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull EventResult eventResult) {
                                Log.i("BRO", eventResult.getType());
                                Log.i("BRO", eventResult.getLatitude());
                                Log.i("BRO", eventResult.getLongitude());

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("BRO", e.getMessage());
                            }
                        }
                );
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
