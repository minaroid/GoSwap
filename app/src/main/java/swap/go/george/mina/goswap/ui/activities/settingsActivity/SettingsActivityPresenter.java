package swap.go.george.mina.goswap.ui.activities.settingsActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;

public class SettingsActivityPresenter implements SettingsActivityMVP.Presenter {

    private SettingsActivityMVP.View view;
    Call<String> call;
    @Override
    public void setView(SettingsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void deleteAccount(String id) {
        Call<ArrayList<Category>> ca = API.getItems().getAllItemsByUser(Integer.parseInt(id));
        call = API.getItems().deleteAccount(id);
        ca.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> categories = response.body();
                for (Category c : categories) {

                    for (Item i : c.getItems()) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("private").child(String.valueOf(i.getItemId())).removeValue();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                view.IsDeleted(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                view.IsDeleted(false);
            }
        });
    }
}
