package swap.go.george.mina.goswap.rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import swap.go.george.mina.goswap.rest.apiModel.Category;

public interface ItemsClient {

    @GET("items-for-swap/")
    Call<ArrayList<Category>> getHomeItems();
}
