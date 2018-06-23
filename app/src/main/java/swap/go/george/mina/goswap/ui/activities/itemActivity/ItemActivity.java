package swap.go.george.mina.goswap.ui.activities.itemActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.mapActivity.MapActivity;
import swap.go.george.mina.goswap.ui.adapters.ImagePagerAdapter;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener{
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
    @BindView(R.id.tv_item_publisher_name)
    TextView publisherName;
    @BindView(R.id.tv_user_ads)
    TextView userAds;
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
        itemDate.setText(item.getDate());
        itemId.setText(String.valueOf(item.getItemId()));
        itemViews.setText(String.valueOf(item.getViews()));
        publisherName.setText(item.getAuthName());
        location.setText(item.getItemCity());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_toolbar, menu);
        return true;
    }

    @OnClick({R.id.callFab,R.id.smsFab,R.id.copyFab,R.id.createContactFab,R.id.tv_item_location
            ,R.id.report_layout,R.id.img_favorite})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.callFab:
                makeCall();
                break;
            case R.id.smsFab:
                makeSMS();
                break;
            case R.id.copyFab:
                makeCopy();
                break;
            case R.id.createContactFab:
                makeContact();
                break;
            case R.id.tv_item_location:
                openMap();
                break;
            case R.id.report_layout:
                makeReport();
                break;
            case R.id.img_favorite:
                makeFavorite();
                break;
        }
    }

    private void makeFavorite() {
        Toast.makeText(this,"makeFavorite",Toast.LENGTH_SHORT).show();
    }

    private void makeReport() {
        Toast.makeText(this,"Reported",Toast.LENGTH_SHORT).show();
    }

    private void openMap() {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("lat",item.getLat());
        i.putExtra("lon",item.getLon());
        startActivity(i);
    }

    private void makeContact() {
        Intent intent = new Intent(
                ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
                ContactsContract.Contacts.CONTENT_URI);
        intent.setData(Uri.parse("tel:"+String.valueOf(item.getPhone())));
        intent.putExtra(Contacts.Intents.Insert.NAME, item.getAuthName());
        intent.putExtra(ContactsContract.Intents.EXTRA_FORCE_CREATE, true);

        startActivity(intent);

    }

    public void makeCopy(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("ddd", String.valueOf(item.getPhone()));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this,"Number Copied",Toast.LENGTH_SHORT).show();
    }

    public void makeCall(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},200);
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + String.valueOf(item.getPhone())));
            startActivity(intent);
        }
    }

    public void makeSMS(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},300);
        }
        else
        {
            Intent it = new Intent(Intent.ACTION_SENDTO,  Uri.parse("smsto:"+String.valueOf(item.getPhone())));
            startActivity(it);
        }
    }
}
