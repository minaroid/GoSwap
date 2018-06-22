package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements HomeFragmentMVP.View
,View.OnClickListener ,SearchView.OnQueryTextListener
        , SearchView.OnCloseListener{

    @BindView(R.id.rv_featured_adds)
    RecyclerView recyclerView;
    @BindView(R.id.fab_add)
    FloatingActionButton floatingActionButton;

    HomeFragmentMVP.Presenter presenter;

    private HomeAdapter adapter;
    private SharedPreferences pref ,userPref ;
    private ArrayList<String> categoriesForSpinner = new ArrayList<>();
    private ArrayList<HomeRecyclerItems> loadedItems = new ArrayList<>() ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new HomeFragmentPresenter();
        presenter.setView(this);
        pref = getActivity().getSharedPreferences("location", MODE_PRIVATE);
        userPref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
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
    }


    @Override
    public void onResume() {
        super.onResume();
        switch (pref.getInt("location",0)){
            case 1:
                 presenter.loadAllCountry();
                 break;
            case 2:
                presenter.loadByGovernate(pref.getString("governate",null));
                break;
            case 3:
                presenter.loadByCity(pref.getString("city",null));
                break;
            default:
                presenter.loadAllCountry();
        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
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
        adapter = new HomeAdapter(getActivity());
        adapter.swapData(mArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openItemActivity(Item item) {
        Toast.makeText(getContext(),item.getDate(),Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.fab_add})
    @Override
    public void onClick(View v) {
        if(userPref.getString("name",null) == null){
            this.showMessage("you must login first");
        }
        else{
            Intent i = new Intent(getActivity(),AddItemActivity.class);
            i.putExtra("categories",categoriesForSpinner);
            startActivity(i);
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
        return true;
    }

    @Override
    public boolean onClose() {
        adapter.swapData(loadedItems);
        return true;
    }
}
