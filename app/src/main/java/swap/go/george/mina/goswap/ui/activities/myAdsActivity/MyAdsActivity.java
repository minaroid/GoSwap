package swap.go.george.mina.goswap.ui.activities.myAdsActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.myItemActivity.MyItemActivity;
import swap.go.george.mina.goswap.ui.adapters.HomeItemsAdapter;

public class MyAdsActivity extends AppCompatActivity implements MyAdsActivityMVP.View{
   @BindView(R.id.rv_my_ads)
    RecyclerView recyclerView;
   @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String id;
    private MyAdsActivityMVP.Presenter presenter;
    private HomeItemsAdapter itemsAdapter;
    private SharedPreferences.Editor deletedPrefEditor;
    private SharedPreferences deletedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Ads");
        id = getIntent().getStringExtra("id");
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemsAdapter = new HomeItemsAdapter(null, this);
        recyclerView.setAdapter(itemsAdapter);
        deletedPrefEditor = getSharedPreferences("itemDeleted", MODE_PRIVATE).edit();
        deletedPref = getSharedPreferences("itemDeleted", MODE_PRIVATE);

        presenter = new MyAdsActivityPresenter();
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (itemsAdapter.getItemCount() == 0) {
            presenter.loadAds(id);
        }

        if (deletedPref.getBoolean("isDeleted", false)) {
            presenter.loadAds(id);
            Log.d("efqef", "fdqwdf");
            deletedPrefEditor.putBoolean("isDeleted", false);
            deletedPrefEditor.commit();
            }
    }

    @Override
    public void openItemActivity(Item item) {
        Intent i = new Intent(this, MyItemActivity.class);
        i.putExtra("item",item);
        startActivity(i);
    }

    @Override
    public void notifyAdapter(ArrayList<Item> items) {
        itemsAdapter.swapData(items);
    }
}
