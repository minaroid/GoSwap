package swap.go.george.mina.goswap.ui.activities.governatesActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Governate;
import swap.go.george.mina.goswap.ui.activities.citiesActivity.CitiesActivity;
import swap.go.george.mina.goswap.ui.adapters.GovernatesAdapter;

public class GovernateActivity extends AppCompatActivity implements GovernatesActivityMVP.View, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_governates)
    RecyclerView recyclerView;
    @BindView(R.id.tv_all_country)
    LinearLayout allCountry;
    private GovernatesActivityMVP.Presenter presenter;
    private GovernatesAdapter adapter;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_governate);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new GovernateActivityPresenter();
        presenter.setView(this);
        presenter.loadData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GovernatesAdapter(this);
        recyclerView.setAdapter(adapter);
        allCountry.setOnClickListener(this);
        editor = getSharedPreferences("location", MODE_PRIVATE).edit();
    }

    @Override
    public void notifyAdapter(ArrayList<Governate> governates) {
        adapter.swapData(governates);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void openCitiesActivity(String governate, ArrayList<String> cities) {
        Intent i = new Intent(this, CitiesActivity.class);
        i.putExtra("governate",governate);
        i.putExtra("cities",cities);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home :
                this.onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_all_country :
                editor.putInt("location", 1);
                editor.apply();
                finish();
                break;
        }
    }
}
