package swap.go.george.mina.goswap.ui.activities.locationActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.Governate;

public class LocationActivityPresenter implements LocationActivityMVP.Presenter{

     private LocationActivityMVP.View view;

    @Override
    public void loadData() {
        Call<ArrayList<Governate>> call = API.getItems().getGovernates();

        call.enqueue(new Callback<ArrayList<Governate>>() {
            @Override
            public void onResponse(Call<ArrayList<Governate>> call, Response<ArrayList<Governate>> response) {
                ArrayList<Governate> governate = response.body();
                view.notifyAdapter(governate);
            }

            @Override
            public void onFailure(Call<ArrayList<Governate>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void setView(LocationActivityMVP.View view) {
          this.view = view;
    }
}
