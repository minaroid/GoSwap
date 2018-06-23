package swap.go.george.mina.goswap.ui.fragments.favoritesFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;
import swap.go.george.mina.goswap.ui.adapters.HomeItemsAdapter;

public class FavoritesFragment extends Fragment {

    @BindView(R.id.rv_favo_list)
    RecyclerView  recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

    }


    @Override
    public void onResume() {
        super.onResume();
        List<Item> list = HomeActivity.appDB.itemDao().getItems();
        recyclerView.setAdapter(new HomeItemsAdapter(new ArrayList<Item>(list),getContext()));
    }
}
