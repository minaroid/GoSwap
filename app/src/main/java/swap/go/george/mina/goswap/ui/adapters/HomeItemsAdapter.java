package swap.go.george.mina.goswap.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import swap.go.george.mina.goswap.R;
import swap.go.george.mina.goswap.rest.apiModel.Item;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.MyViewHolder>{
    private ArrayList<Item> items;
    private Context mContext;
    private String baseImageUrl = "http://192.168.1.3:5000";
    public HomeItemsAdapter(ArrayList<Item> items,Context mContext) {
        this.items = items;
        this.mContext = mContext;
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
        holder.date.setText(item.getDate());
                Glide.with(mContext)
                .asBitmap()
                .load(baseImageUrl+item.getItemPics().get(0))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView title;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.iv_image)
        ImageView image;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}
