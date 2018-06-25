package swap.go.george.mina.goswap.ui.activities.myAdsActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.API;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.itemActivity.ItemActivity;
import swap.go.george.mina.goswap.ui.activities.myItemActivity.MyItemActivity;
import swap.go.george.mina.goswap.ui.adapters.HomeItemsAdapter;

public class MyAdsActivity extends AppCompatActivity implements MyAdsActivityMVP.View{
   @BindView(R.id.rv_my_ads)
    RecyclerView recyclerView;
   @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<Item> items = new ArrayList<>();
    String id ;
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

        Call<ArrayList<Category>> call =  call = API.getItems().getAllItemsByUser(Integer.parseInt(id));
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> categories = response.body();

                for (Category c : categories){
                    items.addAll(c.getItems());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    recyclerView.setAdapter(new HomeItemsAdapter(items,this));
    }


    @Override
    public void openItemActivity(Item item) {
        Intent i = new Intent(this, MyItemActivity.class);
        i.putExtra("item",item);
        startActivity(i);
    }
}
