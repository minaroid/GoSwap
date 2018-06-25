package swap.go.george.mina.goswap.ui.activities.myItemActivity;


import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;

public class MyItemActivityPresenter implements MyItemActivityMVP.Presenter{
    MyItemActivityMVP.View view;

    @Override
    public void setView(MyItemActivityMVP.View view) {
     this.view = view;
    }

    @Override
    public void deleteItem(int id) {
        Call<String> call = API.getItems().deleteItem(String.valueOf(id));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        view.finishActivity();
    }
}
