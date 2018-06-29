package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.HomeRecyclerItems;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.root.App;
import swap.go.george.mina.goswap.ui.activities.addItemActivity.AddItemActivity;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;
import swap.go.george.mina.goswap.ui.adapters.HomeAdapter;
import swap.go.george.mina.goswap.utils.CommonUtils;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements HomeFragmentMVP.View
,View.OnClickListener ,SearchView.OnQueryTextListener
        , SearchView.OnCloseListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_featured_adds)
    RecyclerView recyclerView;
    @BindView(R.id.btn_swap)
    LinearLayout btnSwap;
    @BindView(R.id.no_connection_layout)
    LinearLayout noConnection;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swapRefresh;
    @BindView(R.id.fragment_home)
    RelativeLayout fragmentLayout;
    private  HomeFragmentMVP.Presenter presenter;

    private HomeAdapter adapter;
    private SharedPreferences pref ,userPref ;
    private SharedPreferences.Editor prefEditor;
    private ArrayList<String> categoriesForSpinner = new ArrayList<>();
    private ArrayList<HomeRecyclerItems> loadedItems = new ArrayList<>() ;
    private CommonUtils utils = new CommonUtils();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new HomeFragmentPresenter();
        presenter.setView(this);
        pref = getActivity().getSharedPreferences("location", MODE_PRIVATE);
        prefEditor = pref.edit();
        userPref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        adapter = new HomeAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        swapRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

           if (loadedItems.isEmpty()) {
               if (utils.checkConnection(getContext())) {
                   loadData();
               } else {
                   noConnection.setVisibility(View.VISIBLE);
                   btnSwap.setVisibility(View.GONE);
                   Toast.makeText(getContext(), R.string.msg_no_connection, Toast.LENGTH_SHORT).show();
               }
           } else {
               recyclerView.setAdapter(adapter);
           }

        if(pref.getBoolean("locationIsChanged",false)) {
               loadData();
               prefEditor.putBoolean("locationIsChanged",false);
               prefEditor.commit();
        }
    }

    @Override
    public void showMessage(int msg) {

        switch (msg){

            case 0:
                Toast.makeText(getContext(), R.string.msg_must_login,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void notifyAdapter(ArrayList<Category> categories) {

        ArrayList<HomeRecyclerItems> mArrayList = new ArrayList<>();
//        mArrayList.add(new HomeRecyclerItems("", "", "special",null));
//        mArrayList.add(new HomeRecyclerItems("Ads", "Selected Ads for you", "ads",null));
        categoriesForSpinner.clear();
        for(Category i : categories){
            categoriesForSpinner.add(i.getCategory());
            if(i.getItems().size() != 0) {
                mArrayList.add(new HomeRecyclerItems(i.getCategory(),"", "normal",i.getItems()));
        }}
        loadedItems = mArrayList;
        adapter.swapData(mArrayList);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.btn_swap,R.id.no_connection_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_swap:
                if(utils.checkConnection(getContext())){
                if(userPref.getString("name",null) == null){
                    this.showMessage(0);
                }
                else{
                    Intent i = new Intent(getActivity(),AddItemActivity.class);
                    i.putExtra("categories",categoriesForSpinner);
                    startActivity(i);
                }}
                else {
                    Toast.makeText(getContext(),R.string.msg_no_connection,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.no_connection_layout:
                if(utils.checkConnection(getContext())){
                    noConnection.setVisibility(View.GONE);
                    btnSwap.setVisibility(View.VISIBLE);
                    loadData();
                }
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(HomeFragment.this);
        searchView.setOnCloseListener(HomeFragment.this);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       if(loadedItems != null && adapter !=null) {
           newText = newText.toLowerCase();
           ArrayList<HomeRecyclerItems> newList = new ArrayList<HomeRecyclerItems>();
           ArrayList<HomeRecyclerItems> oldLis = loadedItems;
           for (HomeRecyclerItems item : oldLis) {
               String cate = item.getHeader().toLowerCase();
               if (cate.contains(newText)) {
                   newList.add(item);
               }
           }
           adapter.swapData(newList);
       }
        return true;
    }

    @Override
    public boolean onClose() {
        adapter.swapData(loadedItems);
        return true;
    }

    public void loadData(){
        switch (pref.getInt("location",0)){
            case 1:
                presenter.loadAllCountry();
                swapRefresh.setRefreshing(false);
                break;
            case 2:
                presenter.loadByGovernate(pref.getString("governate",null));
                swapRefresh.setRefreshing(false);
                break;
            case 3:
                presenter.loadByCity(pref.getString("city",null));
                swapRefresh.setRefreshing(false);
                break;
            default:
                presenter.loadAllCountry();
                swapRefresh.setRefreshing(false);
        }
    }


    @Override
    public void onRefresh() {
        loadData();

    }
}
