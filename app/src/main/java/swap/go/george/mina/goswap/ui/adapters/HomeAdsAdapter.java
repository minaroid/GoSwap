package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.models.AdsHomeRecyclerItems;

public class HomeAdsAdapter extends RecyclerView.Adapter<HomeAdsAdapter.MyViewHolder> {
    private ArrayList<AdsHomeRecyclerItems> items;
    private Context mcontext;

    public HomeAdsAdapter(ArrayList<AdsHomeRecyclerItems> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mcontext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_it, parent, false);
        return new HomeAdsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Glide.with(mcontext)
//                .load(recyclerItems.get(position).getItemImage())
//                .into(holder.iv_card_graphics);

        holder.title.setText(items.get(position).getTitle());
        holder.price.setText(items.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView title;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.iv_image)
        ImageView image;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}
