package com.unipi.projects.smartalert.Services.Events;

import com.unipi.projects.smartalert.Model.Events.SendEventRequest;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IEventHttp {
    @POST("/events/create")
    Single<EventResult> SendEvent(@Body SendEventRequest request);

    @GET("/events/stats")
    Single<EventUserStatisticsResult> GetUserStatistics(@Query("id") String id);
}
