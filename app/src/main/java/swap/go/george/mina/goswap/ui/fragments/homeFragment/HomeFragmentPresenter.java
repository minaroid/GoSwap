package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;
import swap.go.george.mina.goswap.rest.API;
import retrofit2.Callback;
import swap.go.george.mina.goswap.rest.apiModel.Category;

public class HomeFragmentPresenter implements HomeFragmentMVP.Presenter {

    private HomeFragmentMVP.View view;
    private Subscription subscription = null;
    private Call<ArrayList<Category>> call;
    public HomeFragmentPresenter(){
    }

    @Override
    public void setView(HomeFragmentMVP.View v) {
        this.view = v;
    }

    @Override
    public void loadAllCountry() {
            call = API.getItems().getAllItems();
            call.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    ArrayList<Category> categories = response.body();
                    view.notifyAdapter(categories);
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    @Override
    public void loadByGovernate(String governate) {

        call = API.getItems().getAllItemsByGovernate(governate);
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> categories = response.body();
                view.notifyAdapter(categories);
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadByCity(String city) {

        call = API.getItems().getAllItemsByCity(city);
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> categories = response.body();
                view.notifyAdapter(categories);
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void rxUnsubscribe() {
        if(view != null){
            if(!subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
        }
    }
    @Override
    public HomeFragmentMVP.View getView() {
        return view;
    }
}
