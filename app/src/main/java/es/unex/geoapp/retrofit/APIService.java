package es.unex.geoapp.retrofit;


import es.unex.geoapp.firebase.FirebaseResponse;
import es.unex.geoapp.firebase.FirebaseSender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAXKsK2vU:APA91bHoA6hUn6DrrRGI9TBSvA7Zft3SQ9QOuuCR7R84eK_oLWy7K0eFUEf6OaNLQfiZkV8owSUHImntDx3rpZ2PVjQr8MqYawoIFJiwOlRMUHwl8qGKOyiTZS_bfmPT2HDi-zuPgC2p"
    })
    @POST( "fcm/send")
    Call<FirebaseResponse> sendNotification(@Body FirebaseSender body);

}
