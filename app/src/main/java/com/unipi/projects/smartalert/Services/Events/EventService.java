package com.unipi.projects.smartalert.Services.Events;

import com.unipi.projects.smartalert.Model.Events.EventUserStatisticsRequest;
import com.unipi.projects.smartalert.Model.Events.SendEventRequest;
import com.unipi.projects.smartalert.Services.Auth.AuthResult;
import com.unipi.projects.smartalert.Services.Auth.IAuthHttp;
import com.unipi.projects.smartalert.Services.RetrofitService;

import io.reactivex.rxjava3.core.Single;

public class EventService implements IEventService{


    @Override
    public Single<EventResult> SendEvent(String type, String latitude, String longitude, String user_id) {
        SendEventRequest sendEventRequest = new SendEventRequest();

        sendEventRequest.setType(type);
        sendEventRequest.setLatitude(latitude);
        sendEventRequest.setLongitude(longitude);
        sendEventRequest.setUserId(user_id);

        IEventHttp eventHttp = RetrofitService.retrofit.create(IEventHttp.class);

        Single<EventResult> eventCall = eventHttp.SendEvent(sendEventRequest);

        return eventCall;
    }

    @Override
    public Single<EventResult> SendEvent(String type, String latitude, String longitude, String comment, String user_id) {
        SendEventRequest sendEventRequest = new SendEventRequest();

        sendEventRequest.setType(type);
        sendEventRequest.setLatitude(latitude);
        sendEventRequest.setLongitude(longitude);
        sendEventRequest.setComment(comment);
        sendEventRequest.setUserId(user_id);

        IEventHttp eventHttp = RetrofitService.retrofit.create(IEventHttp.class);

        Single<EventResult> eventCall = eventHttp.SendEvent(sendEventRequest);

        return eventCall;
    }

    @Override
    public Single<EventUserStatisticsResult> GetEventUserStatistics(String id) {
        EventUserStatisticsRequest request = new EventUserStatisticsRequest();

        request.setId(id);

        IEventHttp eventHttp = RetrofitService.retrofit.create(IEventHttp.class);

        Single<EventUserStatisticsResult> eventCall = eventHttp.GetUserStatistics(request.getId());

        return eventCall;
    }
}
