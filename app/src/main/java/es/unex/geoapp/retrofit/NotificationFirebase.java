package es.unex.geoapp.retrofit;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import es.unex.geoapp.messagemanager.NotificationKind;
import es.unex.geoapp.messagemanager.RequestLocationMessage;
import es.unex.geoapp.messagemanager.SendLocationsMessage;
import es.unex.geoapp.model.LocationFrequency;

public class NotificationFirebase {

    @SerializedName("requestLocation")
    @Expose
    public RequestLocationMessage request;

    @SerializedName("sendLocation")
    @Expose
    public SendLocationsMessage reply;


    public NotificationFirebase(RequestLocationMessage request) {
        this.request = request;
    }

    public NotificationFirebase(SendLocationsMessage reply) {
        this.reply = reply;
    }

    public RequestLocationMessage getRequest() {
        return request;
    }

    public SendLocationsMessage getReply(){return reply;}

    /*******/

    /**Notification Sender**/

}


