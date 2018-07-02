package swap.go.george.mina.goswap.ui.activities.mapActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

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
                .from(new LatLng(Float.parseFloat(currentLat), Float.parseFloat(currentLon)))
                .to(location)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            Leg leg = direction.getRouteList().get(0).getLegList().get(0);
                            ArrayList<LatLng> directionsPositionsList = leg.getDirectionPoint();
                            PolylineOptions polygonOptions = DirectionConverter.createPolyline(MapActivity.this,
                                    directionsPositionsList, 5, Color.RED);
                            mMap.addPolyline(polygonOptions);

                        }

                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });
    }
}
