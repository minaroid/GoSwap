package swap.go.george.mina.goswap.ui.activities.homeActivity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.db.AppDB;
import swap.go.george.mina.goswap.models.HomeRecyclerItems;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.itemActivity.ItemActivity;
import swap.go.george.mina.goswap.ui.activities.listActivity.ListActivity;
import swap.go.george.mina.goswap.ui.activities.locationActivity.LocationActivity;
import swap.go.george.mina.goswap.ui.activities.loginActivity.LoginActivity;
import swap.go.george.mina.goswap.ui.activities.myAdsActivity.MyAdsActivity;
import swap.go.george.mina.goswap.ui.activities.settingsActivity.SettingsActivity;
import swap.go.george.mina.goswap.ui.activities.signupActivity.SignUpActivity;
import swap.go.george.mina.goswap.ui.fragments.chatFragment.ChatFragment;
import swap.go.george.mina.goswap.ui.fragments.favoritesFragment.FavoritesFragment;
import swap.go.george.mina.goswap.ui.fragments.homeFragment.HomeFragment;
import swap.go.george.mina.goswap.utils.CommonUtils;
import swap.go.george.mina.goswap.utils.CurrentLocation;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,HomeActivityMVP.View, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;


    LinearLayout loginAndSignUpLayout;
    TextView loginBtn;
    TextView signUpBtn;
    TextView headerUserName;

    private ActionBarDrawerToggle toggle;
    private FragmentManager fragmentManager;
    private String[] fragments;
    private SharedPreferences userPref , locationPref ;
    private SharedPreferences.Editor userEditor ;
    private TextView toolbarTitle;
    private TextView toolbarSubTitle;
    private CircleImageView profileImage ;
    private Menu NavigatinMenuItems;
    private static final int permissionId = 400;
    public static AppDB appDB;
    private CommonUtils utils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        fragments = new String[]{
                HomeFragment.class.getSimpleName(),
                FavoritesFragment.class.getSimpleName(),
                ChatFragment.class.getSimpleName()
        };
        fragmentManager = getSupportFragmentManager();
        locationPref = getSharedPreferences("location", MODE_PRIVATE);

        userEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
        init();

        updateLocation();

        appDB = Room.databaseBuilder(getApplicationContext(),AppDB.class,"itemsDB")
                .allowMainThreadQueries()
                .build();

//        startService(new Intent(this, MessagesService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (locationPref.getInt("location",0)){
            case 1:
                toolbarTitle.setText("Whole Country");
                toolbarSubTitle.setText("");
                break;
            case 2:
                toolbarTitle.setText(locationPref.getString("governate",null));
                toolbarSubTitle.setText("");
                break;
            case 3:
                toolbarSubTitle.setText(locationPref.getString("governate",null));
                toolbarTitle.setText(locationPref.getString("city",null));
                break;
            default:
                toolbarTitle.setText("Whole Country");
        }

        userPref = getSharedPreferences("user", MODE_PRIVATE);
        if(userPref.getString("name",null)!=null){
            loginAndSignUpLayout.setVisibility(View.GONE);
            headerUserName.setVisibility(View.VISIBLE);
            headerUserName.setText(userPref.getString("name",null));
            if(!userPref.getString("fbId",null).equals("0")){
                Glide.with(this)
                        .setDefaultRequestOptions(new RequestOptions()
                                .error(R.drawable.camera_error))
                        .load("https://graph.facebook.com/" +userPref.getString("fbId",null)
                                + "/picture?type=large")
                        .into(profileImage);} else{

                Glide.with(this)
                        .setDefaultRequestOptions(new RequestOptions()
                                .error(R.drawable.camera_error))
                        .load("http://192.168.43.254:5000" + userPref.getString("pic", null))
                        .into(profileImage);
            }
            NavigatinMenuItems.findItem(R.id.drawer_navi_logout).setVisible(true);
            NavigatinMenuItems.findItem(R.id.drawer_navi_my_ads).setVisible(true);
            NavigatinMenuItems.findItem(R.id.drawer_navi_sett).setVisible(true);
        } else {
            logOut();
        }
    }

    public void init(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.bottom_navi_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navi_home:
                        inflateFragment(0);
                        break;
                    case R.id.bottom_navi_fav:
                        inflateFragment(1);
                        break;
                    case R.id.bottom_navi_msg:
                        inflateFragment(2);
                        break;
                }
                return true;
            }
        });

        inflateFragment(0);

        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarSubTitle = toolbar.findViewById(R.id.toolbar_sub_title);
        toolbarTitle.setOnClickListener(this);
        toolbarSubTitle.setOnClickListener(this);

        View header = navigationView.getHeaderView(0);
        loginBtn = header.findViewById(R.id.tv_login);
        signUpBtn = header.findViewById(R.id.tv_signup);
        loginAndSignUpLayout = header.findViewById(R.id.linear_headers_btns);
        headerUserName = header.findViewById(R.id.tv_user_name);
        profileImage = header.findViewById(R.id.img_profile);
        NavigatinMenuItems = navigationView.getMenu();
        NavigatinMenuItems.findItem(R.id.drawer_navi_logout).setVisible(false);
        NavigatinMenuItems.findItem(R.id.drawer_navi_my_ads).setVisible(false);
        NavigatinMenuItems.findItem(R.id.drawer_navi_sett).setVisible(false);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

    }

    public void inflateFragment(int index){

        String tag;
        Fragment selectedFragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (index){
            case 0:
                tag = fragments[index];
                selectedFragment = fragmentManager.findFragmentByTag(tag);
                if (selectedFragment == null) {
                    selectedFragment = new HomeFragment();
                    transaction.addToBackStack(tag);
                }
                transaction.replace(R.id.home_container, selectedFragment, tag).commit();
                break;
            case 1:
                tag = fragments[index];
                selectedFragment = fragmentManager.findFragmentByTag(tag);
                if (selectedFragment == null) {
                    selectedFragment = new FavoritesFragment();
                    transaction.addToBackStack(tag);
                }
                transaction.replace(R.id.home_container, selectedFragment, tag).commit();
                break;
            case 2:
                tag = fragments[index];
                selectedFragment = fragmentManager.findFragmentByTag(tag);
                if (selectedFragment == null) {
                    selectedFragment = new ChatFragment();
                    transaction.addToBackStack(tag);
                }
                transaction.replace(R.id.home_container, selectedFragment, tag).commit();
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawer_navi_logout :
                logOut();
                break;
            case R.id.drawer_navi_fav:
                bottomNavigationView.setSelectedItemId(R.id.bottom_navi_fav);
                inflateFragment(1);
                break;
            case R.id.drawer_navi_home:
                bottomNavigationView.setSelectedItemId(R.id.bottom_navi_home);
                inflateFragment(0);
                break;
            case R.id.drawer_navi_msg:
                bottomNavigationView.setSelectedItemId(R.id.bottom_navi_msg);
                inflateFragment(2);
                break;
            case R.id.drawer_navi_my_ads:
                if(utils.checkConnection(this)) {
                    Intent i = new Intent(this, MyAdsActivity.class);
                    i.putExtra("id", userPref.getString("id", null));
                    startActivity(i);
                }else{
                    Toast.makeText(this,R.string.msg_no_connection,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.drawer_navi_supp:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"goswap2018@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Problem");
                i.putExtra(Intent.EXTRA_TEXT   , "MY ID : "+userPref.getString("id",null)+"\n"+"my problem : ");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, R.string.msg_email_not_installed, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.drawer_navi_sett:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                Toast.makeText(this,"df",Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void openListActivity(HomeRecyclerItems c) {
        Intent i = new Intent(this, ListActivity.class);
        i.putExtra("items",c.getItems());
        i.putExtra("header",c.getHeader());
        startActivity(i);
    }

    @Override
    public void showMessage(String msg) {
       Snackbar.make(drawerLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openItemActivity(Item item) {
        Intent i = new Intent(this, ItemActivity.class);
        i.putExtra("item",item);
        startActivity(i);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.toolbar_title :
                startActivity(new Intent(HomeActivity.this, LocationActivity.class));
                break;
            case R.id.toolbar_sub_title :
                startActivity(new Intent(HomeActivity.this, LocationActivity.class));
                break;
            case R.id.tv_login:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;
            case R.id.tv_signup:
                startActivity(new Intent(this,SignUpActivity.class));
                break;

        }
    }

    public void logOut(){
        userEditor.clear();
        userEditor.commit();
        loginAndSignUpLayout.setVisibility(View.VISIBLE);
        headerUserName.setVisibility(View.GONE);
        profileImage.setImageResource(R.drawable.ic_login);
        NavigatinMenuItems.findItem(R.id.drawer_navi_logout).setVisible(false);
        NavigatinMenuItems.findItem(R.id.drawer_navi_my_ads).setVisible(false);
        NavigatinMenuItems.findItem(R.id.drawer_navi_sett).setVisible(false);
    }

    public void updateLocation(){
        if(checkLocationPermission()){
        CurrentLocation location = new CurrentLocation(this);
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
                updateLocation();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START); }
        else { finish(); }
    }



}
