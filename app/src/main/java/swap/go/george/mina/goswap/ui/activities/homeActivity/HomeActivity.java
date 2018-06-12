package swap.go.george.mina.goswap.ui.activities.homeActivity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.ui.fragments.chatFragment.ChatFragment;
import swap.go.george.mina.goswap.ui.fragments.favoritesFragment.FavoritesFragment;
import swap.go.george.mina.goswap.ui.fragments.homeFragment.HomeFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private ActionBarDrawerToggle toggle;
    private FragmentManager fragmentManager;
    private String[] fragments;

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

        init();

    }

    public void init(){

        setSupportActionBar(toolbar);
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
            default:
                Toast.makeText(this,"df",Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
        super.onBackPressed();
        }
    }
}
