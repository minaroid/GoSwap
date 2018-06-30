package swap.go.george.mina.goswap.ui.activities.settingsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.rest.API;

public class SettingsActivityPresenter implements SettingsActivityMVP.Presenter {

    private SettingsActivityMVP.View view;

    @Override
    public void setView(SettingsActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void deleteAccount(String id) {
        Call<String> call = API.getItems().deleteAccount(id);
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
