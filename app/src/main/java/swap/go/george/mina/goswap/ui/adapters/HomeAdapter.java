package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.AdsHomeRecyclerItems;
import swap.go.george.mina.goswap.models.HomeRecyclerItems;
import swap.go.george.mina.goswap.models.SpecialHomeRecyclerItems;
import swap.go.george.mina.goswap.rest.apiModel.Category;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivityMVP;

public class HomeAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final ArrayList<HomeRecyclerItems> homeRecyclerItems = new ArrayList<>();
    private final Context context;
    private HomeActivityMVP.View activityView ;
    LinearLayoutManager layoutManager ;


    public HomeAdapter( final Context context) {
        this.context = context;
        this.activityView = (HomeActivityMVP.View) context;

//        this.layoutManager = new LinearLayoutManager(context) {
//            @Override
//            public void smoothScrollToPosition(RecyclerView recyclerView,RecyclerView.State state, int position) {
//                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {
//                    private static final float SPEED = 4000f;// Change this value (default=25f)
//                    @Override
//                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                        return SPEED / displayMetrics.densityDpi;
//                    }
//                };
//                smoothScroller.setTargetPosition(position);
//                startSmoothScroll(smoothScroller);
//            }
//
//        };
//        this.layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_special, parent, false);
                return new SpecialViewHolder(view);

            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_and_cate, parent, false);
                return new MyViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_and_cate, parent, false);
                return new MyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HomeRecyclerItems homeItemModel = homeRecyclerItems.get(position);

        if (homeItemModel != null) {
            switch (homeItemModel.getType()) {

                case "special":
                    RecyclerView re = ((SpecialViewHolder) holder).special_recycler_view;
                    re.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    ArrayList<SpecialHomeRecyclerItems> specialList = loadSpecialList();
                    HomeSpecialAdapter adapter = new HomeSpecialAdapter(specialList);
//                    re.setLayoutManager(layoutManager);
                    re.setAdapter(adapter);
                    break;
                case "ads":
//                    ((MyViewHolder) holder).tv_card_header.setText(homeRecyclerItems.get(position).getHeader());
//                    ((MyViewHolder) holder).tv_card_sub_header.setText(homeRecyclerItems.get(position).getSubHeader());
//                    RecyclerView re2 = ((MyViewHolder) holder).recycler_view;
//                    re2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//                    ArrayList<AdsHomeRecyclerItems> adsList = loadAdsList();
//                    HomeAdsAdapter adapter2 = new HomeAdsAdapter(adsList);
//                    re2.setAdapter(adapter2);
//                    ((MyViewHolder) holder).btn_more.setTag(homeRecyclerItems.get(position));
//                    ((MyViewHolder) holder).btn_more.setOnClickListener(this);
                    break;
                case "normal":
                    ((MyViewHolder) holder).tv_card_header.setText(homeRecyclerItems.get(position).getHeader());
                    ((MyViewHolder) holder).tv_card_sub_header.setText(homeRecyclerItems.get(position).getSubHeader());
                    RecyclerView re3 = ((MyViewHolder) holder).recycler_view;
                    re3.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    HomeItemsAdapter adapter3 = new HomeItemsAdapter(homeRecyclerItems.get(position).getItems(),context);
                    re3.setAdapter(adapter3);
                    ((MyViewHolder) holder).btn_more.setTag(homeRecyclerItems.get(position));
                    ((MyViewHolder) holder).btn_more.setOnClickListener(this);
                    ((MyViewHolder) holder).adsCount.setText(String.valueOf(homeRecyclerItems.get(position).getItems().size())+" ads");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return homeRecyclerItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        switch (homeRecyclerItems.get(position).getType()) {
            case "special":
                return 1;
            case "ads":
                return 2;
            case "normal":
                return 3;
            default:
                return -1;
        }
    }

    public void swapData(ArrayList<HomeRecyclerItems> homeRecycler){
        homeRecyclerItems.clear();
        homeRecyclerItems.addAll(homeRecycler);
        notifyDataSetChanged();
    }
    ///
    private ArrayList<SpecialHomeRecyclerItems> loadSpecialList() {
        ArrayList<SpecialHomeRecyclerItems> mArrayList = new ArrayList<>();

        if (context != null) {
            mArrayList.add(new SpecialHomeRecyclerItems("Awesome Cricket Games", "Enjoy seasonal clones and updates", R.drawable.camera_loading, "1/4"));
            mArrayList.add(new SpecialHomeRecyclerItems("World Heath Day", "Discover clones for a healthy life", R.drawable.camera_loading, "2/4"));
            mArrayList.add(new SpecialHomeRecyclerItems("Flat 50% off on clones", "Life stories of clone legends", R.drawable.camera_loading, "3/4"));
            mArrayList.add(new SpecialHomeRecyclerItems("Clone on Big Screen", "Clones about the sport and players", R.drawable.camera_loading, "4/4"));
        }

        return mArrayList;
    }

    private ArrayList<AdsHomeRecyclerItems> loadAdsList() {
        ArrayList<AdsHomeRecyclerItems> mArrayList = new ArrayList<>();

        if (context != null) {
//            mArrayList.add(new AdsHomeRecyclerItems("mobile-1", "500 $", R.drawable.ic_launcher_background));
//            mArrayList.add(new AdsHomeRecyclerItems("mobile-2", "200 $", R.drawable.ic_launcher_background));
//            mArrayList.add(new AdsHomeRecyclerItems("mobile-3", "100 $", R.drawable.ic_launcher_background));
//            mArrayList.add(new AdsHomeRecyclerItems("mobile-4", "50 $", R.drawable.ic_launcher_background));
        }

        return mArrayList;
    }

    @Override
    public void onClick(View v) {
        activityView.openListActivity((HomeRecyclerItems) v.getTag());
    }

    /// Holders
    class SpecialViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_view)
        RecyclerView special_recycler_view;

        public SpecialViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.header)
        TextView tv_card_header;
        @BindView(R.id.sub_header)
        TextView tv_card_sub_header;
        @BindView(R.id.more)
        TextView btn_more;
        @BindView(R.id.recycler_view)
        RecyclerView recycler_view;
        @BindView(R.id.tv_ads_count)
        TextView adsCount;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

}
