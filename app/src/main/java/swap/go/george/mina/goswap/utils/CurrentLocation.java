package swap.go.george.mina.goswap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by minageorge on 2/7/18.
 */

public class CurrentLocation implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient = null;
    private Location userCurrentLocation = null;
    private Geocoder gcd ;
    private static final String TAG = CurrentLocation.class.getSimpleName();

    private SharedPreferences.Editor userEditor ;
    public CurrentLocation(Context c) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        userEditor = c.getSharedPreferences("currentLocation", MODE_PRIVATE).edit();
        gcd = new Geocoder(c, Locale.ENGLISH);
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
            //noinspection MissingPermission
            userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            userEditor.putString("lat",String.valueOf(userCurrentLocation.getLatitude()));
            userEditor.putString("lon",String.valueOf(userCurrentLocation.getLongitude()));
            userEditor.putString("governate",getUserGovernorate());
            userEditor.putString("city",getUserCity());
            userEditor.commit();

            Call<String> c = API.getItems().updateLocation(getUserGovernorate(),getUserCity());
            c.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

            Log.d(TAG,String.valueOf(userCurrentLocation.getLatitude()));
            Log.d(TAG,String.valueOf(userCurrentLocation.getLongitude()));
            Log.d(TAG,getUserGovernorate());
            Log.d(TAG,getUserCity());

        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public String getUserCity(){
        String city = "";
        try {
            city = gcd.getFromLocation(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude(),1)
                    .get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
          return city;
    }

    public String getUserGovernorate(){
        String governorate = "";
        try {
            governorate = gcd.getFromLocation(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude(),1)
                    .get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return governorate.replace(" Governorate","");
    }
}


