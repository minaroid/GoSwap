package swap.go.george.mina.goswap.ui.activities.itemActivity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.adapters.ImagePagerAdapter;

public class ItemActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.item_name)
    TextView itemTitle;
    @BindView(R.id.pager_indicator)
    CircleIndicator circleIndicator;
    private Item item;
    private ImagePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        item = (Item) getIntent().getSerializableExtra("item");

        init();
    }

    void init(){

        pagerAdapter = new ImagePagerAdapter(this,item.getItemPics());
        viewPager.setAdapter(pagerAdapter);
        itemTitle.setText(item.getItemTitle());
        circleIndicator.setViewPager(viewPager);

    }
}
