package swap.go.george.mina.goswap.ui.activities.locationActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Governate;
import swap.go.george.mina.goswap.ui.adapters.GovernatesAdapter;
import swap.go.george.mina.goswap.utils.CurrentLocation;

public class LocationActivity extends AppCompatActivity implements LocationActivityMVP.View,
        View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.el_governates)
    ExpandableListView expandableListView;
    @BindView(R.id.all_country)
    LinearLayout allCountry;
    @BindView(R.id.current_location)
    LinearLayout currentLocationLayout;
    @BindView(R.id.tv_current_governate)
    TextView textViewCurrentCity;
    @BindView(R.id.tv_current_city)
    TextView textViewCurrentGovernate;
    private LocationActivityMVP.Presenter presenter;
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
        presenter = new LocationActivityPresenter();
        presenter.setView(this);
        adapter = new GovernatesAdapter(this);
        expandableListView.setAdapter(adapter);
        allCountry.setOnClickListener(this);
        currentLocationLayout.setOnClickListener(this);
        currentLocationPref = getSharedPreferences("currentLocation", MODE_PRIVATE);
        editor = getSharedPreferences("location", MODE_PRIVATE).edit();

        if(currentLocationPref.getString("governate",null) != null){
            textViewCurrentCity.setText(currentLocationPref.getString("city",null));
            textViewCurrentGovernate.setText(currentLocationPref.getString("governate",null));
            }
        presenter.loadData();
    }

    @Override
    public void notifyAdapter(ArrayList<Governate> governates) {
        adapter.swapData(governates);
    }


    @Override
    public void wholeCityGovernate(String governate) {
        editor.putBoolean("locationIsChanged",true);
        editor.putInt("location", 2);
        editor.putString("governate", governate);
        editor.apply();
        finish();
    }

    @Override
    public void cityIsSelected(String city, String governate) {
        editor.putBoolean("locationIsChanged",true);
        editor.putInt("location", 3);
        editor.putString("governate",governate);
        editor.putString("city",city);
        editor.apply();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.all_country :
                editor.putBoolean("locationIsChanged",true);
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
                editor.putBoolean("locationIsChanged",true);
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
