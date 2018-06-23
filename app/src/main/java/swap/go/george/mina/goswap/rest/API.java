package swap.go.george.mina.goswap.rest;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {

    private static final String BASE_URL = "http://192.168.1.6:5000/";

    private static Retrofit mRetrofit = null;

    public static Client getItems() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build( );
        }
        return mRetrofit.create(Client.class);
    }
}
