package swap.go.george.mina.goswap.rest;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {


    private static final String BASE_URL = "http://192.168.1.7:5000/";

    private static Retrofit mRetrofit = null;

    public static ItemsClient getItems() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build( );
        }
        return mRetrofit.create(ItemsClient.class);
    }
}
