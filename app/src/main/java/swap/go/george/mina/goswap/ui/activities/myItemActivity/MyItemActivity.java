package swap.go.george.mina.goswap.ui.activities.myItemActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.mapActivity.MapActivity;
import swap.go.george.mina.goswap.ui.adapters.ImagePagerAdapter;

public class MyItemActivity extends AppCompatActivity implements View.OnClickListener
,MyItemActivityMVP.View{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.item_name)
    TextView itemTitle;
    @BindView(R.id.tv_description)
    TextView description;
    @BindView(R.id.tv_item_location)
    TextView location;
    @BindView(R.id.tv_item_id)
    TextView itemId;
    @BindView(R.id.tv_views)
    TextView itemViews;
    @BindView(R.id.tv_date)
    TextView itemDate;
    @BindView(R.id.pager_indicator)
    CircleIndicator circleIndicator;

    private Item item;
    private ImagePagerAdapter pagerAdapter;
    private SharedPreferences userPref ;
    private SharedPreferences.Editor deletedPref;
    private MyItemActivityMVP.Presenter presenter;

    DialogInterface.OnClickListener dialogClickListener =
            new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    presenter.deleteItem(item.getItemId());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        item = (Item) getIntent().getSerializableExtra("item");
        deletedPref = getSharedPreferences("itemDeleted", MODE_PRIVATE).edit();
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        presenter = new MyItemActivityPresenter();
        presenter.setView(this);
        init();

    }
    void init(){

        pagerAdapter = new ImagePagerAdapter(this, item.getItemPics(), false);
        viewPager.setAdapter(pagerAdapter);
        itemTitle.setText(item.getItemTitle());
        circleIndicator.setViewPager(viewPager);
        itemDate.setText(item.getDate());
        itemId.setText(String.valueOf(item.getItemId()));
        itemViews.setText(String.valueOf(item.getViews()));
        location.setText(item.getItemCity());
        description.setText(item.getItemDesc());

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
    @OnClick({R.id.tv_item_location,R.id.btn_delete})
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_item_location:
                openMap();
                break;

            case R.id.btn_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want delete this Item")
                        .setPositiveButton("delete", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
        }
    }

    private void openMap() {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("lat",item.getLat());
        i.putExtra("lon",item.getLon());
        startActivity(i);
    }

    @Override
    public void finishActivity() {
        deletedPref.putBoolean("isDeleted", true);
        deletedPref.commit();
        this.finish();
    }
}
