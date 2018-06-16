package swap.go.george.mina.goswap.rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Governate;

public interface ItemsClient {

    @GET("all_items-for-swap/")
    Call<ArrayList<Category>> getAllItems();

    @GET("governate_items-for-swap/{goverquery}/")
    Call<ArrayList<Category>> getAllItemsByGovernate(@Path("goverquery") String query);

    @GET("city_items-for-swap/{cityquery}/")
    Call<ArrayList<Category>> getAllItemsByCity(@Path("cityquery") String query);

    @GET("governates/")
    Call<ArrayList<Governate>> getGovernates();

}
