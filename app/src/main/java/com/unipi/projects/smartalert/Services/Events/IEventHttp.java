package com.unipi.projects.smartalert.Services.Events;

import com.unipi.projects.smartalert.Model.Events.SendEventRequest;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IEventHttp {
    @POST("/events/create")
    Single<EventResult> SendEvent(@Body SendEventRequest request);

}
