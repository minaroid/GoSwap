package swap.go.george.mina.goswap.ui.activities.myAdsActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;

public class MyAdsActivityPresenter implements MyAdsActivityMVP.Presenter {

    private MyAdsActivityMVP.View view;

    @Override
    public void setView(MyAdsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void loadAds(String id) {
        Call<ArrayList<Category>> call = call = API.getItems().getAllItemsByUser(Integer.parseInt(id));
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> categories = response.body();
                ArrayList<Item> items = new ArrayList<>();
                for (Category c : categories) {
                    items.addAll(c.getItems());
                }
                view.notifyAdapter(items);
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
