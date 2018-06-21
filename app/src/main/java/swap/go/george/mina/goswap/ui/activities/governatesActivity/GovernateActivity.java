package swap.go.george.mina.goswap.ui.activities.governatesActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import swap.go.george.mina.goswap.utils.CurrentLocation;

public class GovernateActivity extends AppCompatActivity implements GovernatesActivityMVP.View, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_governates)
    RecyclerView recyclerView;
    @BindView(R.id.all_country)
    LinearLayout allCountry;
    @BindView(R.id.current_location)
    LinearLayout currentLocationLayout;
    @BindView(R.id.tv_current_governate)
    TextView textViewCurrentCity;
    @BindView(R.id.tv_current_city)
    TextView textViewCurrentGovernate;
    private GovernatesActivityMVP.Presenter presenter;
    private GovernatesAdapter adapter;
    private SharedPreferences.Editor editor;
    private SharedPreferences currentLocationPref;
    private static final int permissionId = 700;
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
        currentLocationLayout.setOnClickListener(this);
        currentLocationPref = getSharedPreferences("currentLocation", MODE_PRIVATE);
        editor = getSharedPreferences("location", MODE_PRIVATE).edit();

        if(currentLocationPref.getString("governate",null) != null){
            textViewCurrentCity.setText(currentLocationPref.getString("city",null));
            textViewCurrentGovernate.setText(currentLocationPref.getString("governate",null));
            }

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
            case R.id.all_country :
                editor.putInt("location", 1);
                editor.apply();
                finish();
                break;
            case  R.id.current_location:
                if(currentLocationPref.getString("governate",null) == null){
                    if(checkLocationPermission()){
                        CurrentLocation location = new CurrentLocation(this);
                    }
                }
                editor.putInt("location", 3);
                editor.putString("governate",currentLocationPref.getString("governate",null));
                editor.putString("city",currentLocationPref.getString("city",null));
                editor.apply();
                finish();
                break;

        }
    }


    public boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, permissionId);
        } else {
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionId) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              CurrentLocation  location = new CurrentLocation(this);
            }
        }
    }

}
