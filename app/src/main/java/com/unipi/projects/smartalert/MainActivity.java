package com.unipi.projects.smartalert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.projects.smartalert.Services.Auth.AuthResult;
import com.unipi.projects.smartalert.Services.Auth.AuthService;
import com.unipi.projects.smartalert.Services.Events.EventResult;
import com.unipi.projects.smartalert.Services.Events.EventService;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting data from intent
        String email = getIntent().getStringExtra("email");
        String userId = getIntent().getStringExtra("userId");

        setupSpinnerSelection();

        EditText editTextMultiLine = findViewById(R.id.editTextMultiLine);

        Button sendEventButton = findViewById(R.id.sendEventButton);

        sendEventButton.setOnClickListener(
                (view) -> {
                    Spinner spinner = findViewById(R.id.selectTypeSpinner);
                    String selectedType = spinner.getSelectedItem().toString();

                    EventService eventService = new EventService();

                    if(editTextMultiLine.getText().toString().isEmpty()) {
                        Single<EventResult> singleEventResult = eventService
                                .SendEvent(
                                    selectedType,
                                    "132",
                                    "123",
                                        userId
                                );
                        singleEventResultSubscription(singleEventResult);
                    }
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
}
