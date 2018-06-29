package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivity;
import swap.go.george.mina.goswap.ui.activities.homeActivity.HomeActivityMVP;
import swap.go.george.mina.goswap.ui.activities.listActivity.ListActivity;
import swap.go.george.mina.goswap.ui.activities.listActivity.ListActivityMVP;
import swap.go.george.mina.goswap.ui.activities.myAdsActivity.MyAdsActivity;
import swap.go.george.mina.goswap.ui.activities.myAdsActivity.MyAdsActivityMVP;
import swap.go.george.mina.goswap.ui.activities.userAdsActivity.UserAdsActivity;
import swap.go.george.mina.goswap.ui.activities.userAdsActivity.UserAdsActivityMVP;
import swap.go.george.mina.goswap.utils.CommonUtils;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.MyViewHolder>{
    private ArrayList<Item> items;
    private Context mContext;
    private String baseImageUrl = "http://192.168.1.4:5000";
    private HomeActivityMVP.View homeView;
    private ListActivityMVP.View listActivityView;
    private MyAdsActivityMVP.View myAdsView;
    private UserAdsActivityMVP.View userAdsView;
    CommonUtils utils = new CommonUtils();
    public HomeItemsAdapter(ArrayList<Item> items,Context mContext) {
        this.items = items;
        this.mContext = mContext;
        if(mContext.getClass() == HomeActivity.class){
        this.homeView = (HomeActivityMVP.View) mContext;
        }
        else if (mContext.getClass() == ListActivity.class){
            this.listActivityView = (ListActivityMVP.View) mContext;
        }
        else if (mContext.getClass() == MyAdsActivity.class){
            this.myAdsView = (MyAdsActivityMVP.View) mContext;
        } else if (mContext.getClass() == UserAdsActivity.class) {
            this.userAdsView = (UserAdsActivityMVP.View) mContext;
        }
    }

    @NonNull
    @Override
    public HomeItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cate_it, parent, false);
        return new HomeItemsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeItemsAdapter.MyViewHolder holder, int position) {
        Item item = items.get(position);
        holder.title.setText(item.getItemTitle());
        holder.date.setText(item.getDate().replace(" 2018",""));
        try{
            Glide.with(mContext)
                    .setDefaultRequestOptions(new RequestOptions()
                    .error(R.drawable.camera_error))
                .load(baseImageUrl+item.getItemPics().get(0))
                .into(holder.image);
        }
                catch(Exception e){
                   e.printStackTrace();
        }
        holder.view.setText(String.valueOf(item.getViews()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public void swapData(ArrayList<Item> items) {
        if (this.items != null) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        } else {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView title;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.tv_view)
        TextView view;
        @BindView(R.id.iv_image)
        ImageView image;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if(utils.checkConnection(mContext)){
            if(homeView != null){
                homeView.openItemActivity(items.get(pos));
            }else if (listActivityView != null){
                listActivityView.openItemActivity(items.get(pos));
            } else if (userAdsView != null) {
                userAdsView.openItemActivity(items.get(pos));
            } else {
                myAdsView.openItemActivity(items.get(pos));
            }
            }
            else {
                Toast.makeText(mContext,R.string.msg_no_connection,Toast.LENGTH_SHORT).show();
            }
        }
    }


}
