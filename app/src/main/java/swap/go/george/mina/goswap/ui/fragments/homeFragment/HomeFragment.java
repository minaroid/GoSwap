package swap.go.george.mina.goswap.ui.fragments.homeFragment;

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

public class HomeFragment extends Fragment implements HomeFragmentMVP.View{

    @BindView(R.id.rv_featured_adds)
    RecyclerView recyclerView;

//    @Inject
    HomeFragmentMVP.Presenter presenter;

    private HomeAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ((App) getActivity().getApplication()).getComponent().injectHomeFragment(this);
        presenter = new HomeFragmentPresenter();
        presenter.setView(this);
        presenter.loadData();
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
