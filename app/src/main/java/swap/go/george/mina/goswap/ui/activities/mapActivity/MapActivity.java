package swap.go.george.mina.goswap.ui.activities.mapActivity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;

public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String lat,lon;
    private GoogleMap mMap;
    private SharedPreferences  locationPref ;
    private String currentLat,currentLon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Map");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lon = getIntent().getStringExtra("lon");
        lat = getIntent().getStringExtra("lat");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationPref = getSharedPreferences("currentLocation", MODE_PRIVATE);
        currentLat = locationPref.getString("lat",null);
        currentLon = locationPref.getString("lon",null);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
        mMap.addMarker(new MarkerOptions().position(location));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10));

        GoogleDirection.withServerKey(getResources().getString(R.string.maps_api_key))
                .from(location)
                .to(new LatLng(Float.parseFloat(currentLat), Float.parseFloat(currentLon)))
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {

                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }
}
