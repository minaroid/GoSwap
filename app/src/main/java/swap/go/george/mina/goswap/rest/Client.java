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
    Call<ArrayList<UserInfo>> signUpUsingFb(@Path("name") String name,
                                            @Path("email") String email,
                                            @Path("fb_id") String pic);

    @GET("login_fb/{email}/")
    Call<ArrayList<UserInfo>> checkLoginFb(@Path("email") String email);

    @GET("login/{email}/{passw}/")
    Call<ArrayList<UserInfo>> checkLogin(@Path("email") String email, @Path("passw") String pass);


    @GET("update_location/{governate}/{city}/")
    Call<String> updateLocation(@Path("governate") String gover, @Path("city") String city);

    @GET("upload_item_data/{item_title}/{item_des}/{item_gover}/{item_city}/{item_cate}/{date}/{auth_id}/{lat}/{lon}/{auth_name}/{phone}/")
    Call<String> uploadItemData( @Path("item_title") String itemTitle,
                                @Path("item_des") String itemDes, @Path("item_gover") String itemGover,
                                @Path("item_city") String itemCity, @Path("item_cate") String itemCate,
                                @Path("date") String date, @Path("auth_id") String authId,
                                @Path("lat") String lat, @Path("lon") String lon,
                                @Path("auth_name") String authName, @Path("phone") String phone);
    @GET("update_views/{itemId}/{userId}/")
    Call<String> updateViews(@Path("itemId")String itemID,@Path("userId")String userId);


    @GET("user_items-for-swap/{userId}/")
    Call<ArrayList<Category>> getAllItemsByUser(@Path("userId") int query);

    @GET("delete_item/{item_id}/")
    Call<String> deleteItem(@Path("item_id") String id);

    @GET("delete_account/{user_id}/")
    Call<String> deleteAccount(@Path("user_id") String userId);
}
