package swap.go.george.mina.goswap.ui.activities.settingsActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SettingsActivityMVP.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_load)
    ProgressBar progressBar;

    private SharedPreferences.Editor userPrefEditor;
    private SharedPreferences userPref;
    private SettingsActivityMVP.Presenter presenter;
    private String userId;

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.deleteAccount(userId);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.btn_delete_account);
        userPrefEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userId = userPref.getString("id", null);
        presenter = new SettingsActivityPresenter();
        presenter.setView(this);
    }

    @OnClick(R.id.btn_delete_account)
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warn_delete_confirm)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void IsDeleted(Boolean isDeleted) {
        userPrefEditor.clear();
        userPrefEditor.commit();
        HomeActivity.appDB.itemDao().truncateTable();
        Call<ArrayList<Category>> call = API.getItems().getAllItemsByUser(Integer.parseInt(userId));
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> categories = response.body();
                int x = 0;
                for (Category c : categories) {

                    for (Item i : c.getItems()) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("private").child(String.valueOf(i.getItemId()))
                                .getParent().removeValue();
                    }

                    x++;
                    if (x == categories.size()) {
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });
    }
}
