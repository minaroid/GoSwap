package swap.go.george.mina.goswap.ui.fragments.homeFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.HomeRecyclerItems;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.root.App;
import swap.go.george.mina.goswap.ui.adapters.HomeAdapter;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements HomeFragmentMVP.View{

    @BindView(R.id.rv_featured_adds)
    RecyclerView recyclerView;

    HomeFragmentMVP.Presenter presenter;

    private HomeAdapter adapter;
    private SharedPreferences pref ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomeFragmentPresenter();
        presenter.setView(this);
        pref = getActivity().getSharedPreferences("location", MODE_PRIVATE);
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
    public void showMessage(int msg) {

    }

    @Override
    public void notifyAdapter(ArrayList<Category> categories) {

        ArrayList<HomeRecyclerItems> mArrayList = new ArrayList<>();

        mArrayList.add(new HomeRecyclerItems("", "", "special",null));

        mArrayList.add(new HomeRecyclerItems("Ads", "Selected Ads for you", "ads",null));

        for(Category i : categories)
            if(i.getItems().size() != 0) {
                mArrayList.add(new HomeRecyclerItems(i.getCategory(),"", "normal",i.getItems()));
        }

        adapter = new HomeAdapter(mArrayList,getContext());
        recyclerView.setAdapter(adapter);
    }
}
