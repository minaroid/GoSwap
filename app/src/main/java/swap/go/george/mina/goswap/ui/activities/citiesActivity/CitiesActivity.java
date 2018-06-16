package swap.go.george.mina.goswap.ui.activities.citiesActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;
import swap.go.george.mina.goswap.ui.adapters.CitiesAdapter;

public class CitiesActivity extends AppCompatActivity implements View.OnClickListener,
CitiesActivityMVP.View{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_cities)
    RecyclerView recyclerView;
    @BindView(R.id.tv_all_gover)
    TextView allGovernate ;
    private ArrayList<String> cities;
    private SharedPreferences.Editor editor;
    private String governate ;
    private CitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        governate =getIntent().getStringExtra("governate");
        getSupportActionBar().setTitle(governate);
        allGovernate.setText("All "+ governate);
        cities = (ArrayList<String>) getIntent().getSerializableExtra("cities");
        allGovernate.setOnClickListener(this);
        editor = getSharedPreferences("location", MODE_PRIVATE).edit();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitiesAdapter(this);
        adapter.swapData(cities,governate);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        editor.putInt("location", 2);
        editor.putString("governate",governate);
        editor.apply();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void selectedCity(String gover, String city) {
        editor.putInt("location", 3);
        editor.putString("governate",gover);
        editor.putString("city",city);
        editor.apply();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
