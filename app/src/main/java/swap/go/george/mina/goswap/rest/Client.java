package swap.go.george.mina.goswap.rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Governate;
import swap.go.george.mina.goswap.rest.apiModel.UserInfo;

public interface Client {

    @GET("all_items-for-swap/ ")
    Call<ArrayList<Category>> getAllItems();

    @GET("governate_items-for-swap/{goverquery}/")
    Call<ArrayList<Category>> getAllItemsByGovernate(@Path("goverquery") String query);

    @GET("city_items-for-swap/{cityquery}/")
    Call<ArrayList<Category>> getAllItemsByCity(@Path("cityquery") String query);

    @GET("governates/")
    Call<ArrayList<Governate>> getGovernates();

    @GET("signup_fb/{name}/{email}/{fb_id}/")
    Call<ArrayList<UserInfo>> signUpUsingFb(@Path("name") String namee,
                                            @Path("email") String emaill,
                                            @Path("fb_id") String picc);
    @GET("login_fb/{email}/")
    Call<ArrayList<UserInfo>> checkLoginFb(@Path("email") String email);

}
