package es.unex.geoapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sender {

    @SerializedName("to")
    @Expose
    public String to;


    @SerializedName("data")
    @Expose
    public NotificationFirebase data;

    public Sender() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationFirebase getData() {
        return data;
    }

    public void setData(NotificationFirebase data) {
        this.data = data;
    }

    public Sender(String to, NotificationFirebase data) {
        this.to = to;
        this.data = data;
    }


}
