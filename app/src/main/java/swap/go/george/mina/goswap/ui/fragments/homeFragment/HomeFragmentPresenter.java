package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import rx.Subscription;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.ItemsClient;
import retrofit2.Callback;
import swap.go.george.mina.goswap.rest.apiModel.Category;

public class HomeFragmentPresenter implements HomeFragmentMVP.Presenter {

    private HomeFragmentMVP.View view;
    private Subscription subscription = null;

    public HomeFragmentPresenter(){
    }

    @Override
    public void setView(HomeFragmentMVP.View v) {
        this.view = v;
    }

    @Override
    public void loadData() {
//        subscription = api.getHomeItems().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HomeItem>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.showMessage(0);
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(HomeItem i) {
//
//                        view.notifyAdapter(i);
//                        Log.d("dddfff",i.toString());
//
//
//                    }
//                });

        Call<ArrayList<Category>> call = API.getItems().getHomeItems();

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
